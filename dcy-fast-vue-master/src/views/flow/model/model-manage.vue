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
        <el-button icon="el-icon-plus" type="primary" v-permission="['model:save']" @click="addModel()">添加</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <table-page
      ref="tablePage"
      url="flow/model/page"
      :columns="columns"
      :query-params="queryParams"
      :check-box="false">
      <template slot-scope="{ row }" slot="action">
        <el-button @click="update(row)" v-permission="['model:update']" type="text">修改</el-button>
        <el-button @click="deploy(row)" v-permission="['model:deploy']" type="text">部署</el-button>
        <el-button @click="remove(row)" v-permission="['model:delete']" type="text">删除</el-button>
      </template>
    </table-page>

    <!-- 添加表单 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogShow"
      v-if="dialogShow"
      append-to-body
      width="80%"
      top="5vh"
      :modal-append-to-body="false"
      :close-on-click-modal="false">
      <workflow-bpmn-modeler v-if="dialogShow"
                             ref="refNode"
                             :xml="xml"
                             :is-view="false"
                             @save="saveModel"/>
    </el-dialog>
  </div>
</template>

<script>
import TablePage from '_c/CommonForm/table-page'
import {ConfirmCustom, MessageError, MessageSuccess} from '_l/message'
import {addModel, deleteModelById, deployModel, getModelJsonById, updateModel} from "_a/flow/model/model";
import {Message} from "element-ui";
import WorkflowBpmnModeler from "@/components/Flowable";


export default {
  name: 'model-manage',
  components: {WorkflowBpmnModeler, TablePage},
  data() {
    return {
      queryParams: {},
      columns: [
        {label: '模型ID', prop: 'id', width: '300px'},
        {label: '名称', prop: 'name'},
        {label: '标识', prop: 'key'},
        {label: '类别', prop: 'category'},
        {label: '版本号', prop: 'version', width: '80px'},
        {label: '创建时间', prop: 'createTime'},
        {label: '更新时间', prop: 'lastUpdateTime'},
        {label: '操作', slot: 'action'}
      ],
      dialogShow: false,
      dialogTitle: '添加模型',
      xml: null,
      modelId: null,
    }
  },
  methods: {
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
    addModel() {
      this.xml = null
      this.dialogTitle = '添加模型'
      this.dialogShow = true
    },
    deploy(row) {
      ConfirmCustom({type: 'warning', message: `是否确认部署名称为【${row.name}】的模型?`,}).then(() => {
        deployModel(row.id).then(res => {
          if (res.data) {
            Message.success({message: '部署成功'})
          } else {
            Message.error({message: '部署失败'})
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
      ConfirmCustom({type: 'warning', message: `是否确认删除名称为【${row.name}】的模型?`,}).then(() => {
        deleteModelById(row.id).then(res => {
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
     * 关闭弹出框 和 刷新表格
     */
    cancelDialogAndRefresh() {
      this.dialogShow = false
      // 刷新表格
      this.refresh()
    },
    saveModel(data) {
      let model = {
        key: data.process.id,
        category: data.process.category,
        name: data.process.name,
        description: data.process.name,
        jsonXml: data.xml,
        svgXml: data.svg,
      }
      if (this.dialogTitle === '添加模型') {
        addModel(model).then(res => {
          if (res.data) {
            MessageSuccess('add')
          } else {
            MessageError('add')
          }
          this.$nextTick(function () {
            // 刷新表格
            this.cancelDialogAndRefresh()
          })
        })
      } else {
        model.id = this.modelId
        updateModel(model).then(res => {
          if (res.data) {
            MessageSuccess('upd')
          } else {
            MessageError('upd')
          }
          this.$nextTick(function () {
            // 刷新表格
            this.cancelDialogAndRefresh()
          })
        })
      }
    },
    /**
     * 修改模型
     * @param row
     */
    update(row) {
      this.modelId = row.id
      this.dialogTitle = '修改模型'
      this.dialogShow = true
      getModelJsonById(row.id).then(res => {
        this.xml = res.data
      })
    }
  },
  mounted() {

  }
}
</script>

<style scoped lang="scss">

</style>
