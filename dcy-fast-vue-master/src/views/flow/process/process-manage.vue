<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form ref="queryParams" inline label-width="60px" label-position="left">
      <el-form-item label="名称">
        <el-input v-model="queryParams.name" clearable placeholder="请输入名称"/>
      </el-form-item>
      <el-form-item label="类别">
        <el-input v-model="queryParams.category" clearable placeholder="请输入类别"/>
      </el-form-item>
      <el-form-item label="标识">
        <el-input v-model="queryParams.key" clearable placeholder="请输入标识"/>
      </el-form-item>
      <el-form-item label-width="0px">
        <el-button icon="el-icon-search" type="primary" @click="refresh()">查询</el-button>
        <el-button icon="el-icon-refresh" type="primary" @click="reset()">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <table-page
      ref="tablePage"
      url="flow/process/page"
      :columns="columns"
      :query-params="queryParams"
      :check-box="false">
      <template slot-scope="{ row }" slot="suspendState">
        <el-tag type="warning" v-if="row.suspendState">已挂起</el-tag>
        <el-tag type="success" v-else>激活</el-tag>
      </template>
      <template slot-scope="{ row }" slot="action">
        <el-button @click="setApprovalUser(row)" v-permission="['process:approve']" type="text">设置审批</el-button>
        <el-button @click="seeProcessPic(row)" v-permission="['process:chart']" type="text">流程图</el-button>
        <el-button @click="update(row)" v-permission="['process:hang:change']" type="text">{{ row.suspendState ? '激活' : '挂起' }}</el-button>
        <el-button @click="remove(row)" v-permission="['process:delete']" type="text">删除</el-button>
      </template>
    </table-page>

    <el-dialog
      title="流程图"
      :v-if="dialogProcessPicShow"
      :visible.sync="dialogProcessPicShow"
      append-to-body
      width="80%"
      :modal-append-to-body="false">
      <el-row>
        <el-col :span="24" style="text-align: center;padding: 20px">
          <el-image :src="processPicUrl" alt=""/>
        </el-col>
      </el-row>
    </el-dialog>


    <el-dialog
      title="流程节点详细"
      :visible.sync="dialogApprovalShow"
      v-if="dialogApprovalShow"
      append-to-body
      width="80%"
      :modal-append-to-body="false">
      <el-row>
        <el-col :span="24" style="text-align: center;padding: 20px">
          <el-image :src="processPicUrl" alt=""/>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-top: 50px">
          <el-table
            border
            ref="flowNodeTable"
            :data="flowNodeTableData"
            style="width: 100%">
            <el-table-column
              prop="flowName"
              label="节点名称"
              align="center"
              width="180">
            </el-table-column>
            <el-table-column
              prop="flowType"
              label="审批类型"
              width="180"
              align="center">
              <template slot-scope="{row}">
                <el-tag v-for="item in flowTypeOptionList" v-if="row.flowType === item.dictValue"
                        :type="item.listClass">
                  {{ item.dictLabel }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column
              prop="ids"
              label="数据">
              <template slot-scope="{row}">
                <el-tag v-for="item in row.ids" size="small">{{ item }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column
              prop="action"
              label="设置">
              <template slot-scope="{row, $index}">
                <el-button @click="setUpFlow(row, $index)" type="text">设置</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveFlowNode()">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="设置审批人"
      v-if="dialogFlowSetShow"
      :visible.sync="dialogFlowSetShow"
      append-to-body
      :modal-append-to-body="false">
      <el-form ref="form" :model="flowNodeRow" label-width="80px">
        <el-form-item label="审批名称">
          <el-input v-model="flowNodeRow.flowName" :disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item label="审批类型">
          <el-select v-model="flowNodeRow.flowType" clearable placeholder="请选择审批类型" style="width: 100%">
            <template v-for="item in flowTypeOptionList">
              <el-option :value="item.dictValue" :label="item.dictLabel"/>
            </template>
          </el-select>
        </el-form-item>
        <el-form-item label="数据">
          <el-select
            v-model="flowNodeRow.ids"
            multiple
            filterable
            remote
            clearable
            reserve-keyword
            placeholder="请输入关键词"
            :remote-method="remoteMethod"
            :loading="loading"
            style="width: 100%">
            <el-option
              v-for="item in optionData"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveData">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import TablePage from '_c/CommonForm/table-page'
import {ConfirmCustom} from "_l/message";
import {Message} from "element-ui";
import {
  deleteProcessByDeploymentId,
  flowNodeSave,
  getActivityListByProDefId,
  hangChange
} from "_a/flow/process/process";
import {mapActions} from "vuex";
import {getOptionDataByFlowType} from "@/api/admin/user/user";

export default {
  name: 'process-manage',
  components: {TablePage},
  data() {
    return {
      dialogProcessPicShow: false,
      dialogApprovalShow: false,
      dialogFlowSetShow: false,
      processPicUrl: null,
      flowNodeTableData: [],
      queryParams: {},
      columns: [
        {label: '流程定义ID', prop: 'id', width: '350px'},
        {label: '名称', prop: 'name'},
        {label: '标识', prop: 'key'},
        {label: '类别', prop: 'category'},
        {label: '版本', prop: 'version', width: '80px'},
        {label: '是否挂起', slot: 'suspendState', width: '80px'},
        {label: '部署时间', prop: 'deploymentTime'},
        {label: '操作', slot: 'action'}
      ],
      flowNodeRow: null,
      flowNodeIndex: null,
      flowTypeOptionList: [],
      idsOptionList: [],
      optionData: [],
      loading: false,
    }
  },
  watch: {
    'flowNodeRow.flowType': function (newVal, oldVal) {
      console.log(oldVal);
      console.log(newVal);
      if (oldVal !== undefined && newVal !== oldVal) {
        this.flowNodeRow.ids = [];
      }
    }
  },
  methods: {
    ...mapActions(['getDictListByType']),
    /**
     * 刷新
     */
    refresh() {
      this.$refs.tablePage.refresh()
    },
    /**
     * 重置搜索条件
     */
    reset() {
      this.queryParams = {}
      this.$nextTick(function () {
        this.refresh()
      })
    },
    seeProcessPic(row) {
      this.dialogProcessPicShow = true;
      let time = new Date().getTime();
      this.processPicUrl = `${process.env.VUE_APP_PROXY_TARGET}flow/diagram/getPicByProcessDefinitionId?processDefinitionId=${row.id}&time=${time}`;
    },
    update(row) {
      ConfirmCustom({type: 'warning', message: `是否确认挂起为${row.id}的流程?`,}).then(() => {
        hangChange(row.id).then(res => {
          if (res.data) {
            Message.success({message: '操作成功'})
          } else {
            Message.error({message: '操作失败'})
          }
          // 刷新表格
          this.refresh()
        })
      })
    },
    /**
     * 行内删除
     * @param row
     */
    remove(row) {
      ConfirmCustom({type: 'warning', message: `是否确认删除为${row.id}的流程?`,}).then(() => {
        deleteProcessByDeploymentId(row.deploymentId).then(res => {
          if (res.data) {
            Message.success({message: '删除成功'})
          } else {
            Message.error({message: '删除失败'})
          }
          // 刷新表格
          this.refresh()
        })
      })
    },
    /**
     * 设置审批人
     * @param row
     */
    setApprovalUser(row) {
      let time = new Date().getTime();
      this.processPicUrl = `${process.env.VUE_APP_PROXY_TARGET}flow/diagram/getPicByProcessDefinitionId?processDefinitionId=${row.id}&time=${time}`;
      getActivityListByProDefId(row.id).then(res => {
        this.flowNodeTableData = res.data
        this.$nextTick(function () {
          this.dialogApprovalShow = true;
        })
      })
    },
    saveFlowNode() {
      this.dialogApprovalShow = false;
      flowNodeSave(this.flowNodeTableData).then(res => {
        if (res.data) {
          // 刷新表格
          this.refresh()
        }
      })
    },
    setUpFlow(row, index) {
      this.flowNodeRow = {...row};
      this.flowNodeIndex = index;
      this.optionData = []
      this.dialogFlowSetShow = true;
    },
    remoteMethod(query) {
      if (query !== '') {
        this.loading = true;
        getOptionDataByFlowType(this.flowNodeRow.flowType, query).then(res => {
          this.loading = false;
          this.optionData = res.data
        })
      } else {
        this.optionData = []
      }
    },
    saveData() {
      this.flowNodeTableData.splice(this.flowNodeIndex, 1, this.flowNodeRow);
      this.dialogFlowSetShow = false;
    }
  },
  mounted() {
    this.getDictListByType('flow_type').then(res => {
      this.flowTypeOptionList = res
    })
  }
}
</script>

<style scoped lang="scss">

</style>
