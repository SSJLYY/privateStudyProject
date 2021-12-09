<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form ref="queryParams" :model="queryParams" inline label-width="80px" label-position="left">
      <el-form-item label="任务名称">
        <el-input v-model="queryParams.jobName" clearable placeholder="请输入任务名称"/>
      </el-form-item>
      <el-form-item label="任务组名">
        <el-select v-model="queryParams.jobGroup" placeholder="请选择任务组名" @input="change($event)" style="width: 100%">
          <template v-for="item in jobGroupStatusOptionList">
            <el-option :value="item.dictValue" :label="item.dictLabel"/>
          </template>
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryParams.jobStatus" placeholder="请选择状态" @input="change($event)" style="width: 100%">
          <template v-for="item in jobStatusOptionList">
            <el-option :value="item.dictValue" :label="item.dictLabel"/>
          </template>
        </el-select>
      </el-form-item>
      <el-form-item label="执行时间">
        <el-date-picker
          v-model="executionDate"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label-width="0px">
        <el-button icon="el-icon-search" type="primary" @click="refresh()">查询</el-button>
        <el-button icon="el-icon-refresh" type="primary" @click="reset()">重置</el-button>
        <el-button icon="el-icon-delete" type="primary" @click="removeBatch()" :disabled="delBtnDisabled">删除</el-button>
        <el-button icon="el-icon-delete" type="primary" @click="clearLog()">清空</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <table-page
      ref="tablePage"
      :url="url"
      :columns="columns"
      :query-params="queryParams"
      @table-select-val="selectVal">
      <template slot-scope="{ row }" slot="jobGroup">
        <el-tag v-for="item in jobGroupStatusOptionList" v-if="row.jobGroup === item.dictValue" :type="item.listClass">
          {{item.dictLabel}}
        </el-tag>
      </template>
      <template slot-scope="{ row }" slot="jobStatus">
        <el-tag v-for="item in jobStatusOptionList" v-if="row.jobStatus === item.dictValue" :type="item.listClass">
          {{item.dictLabel}}
        </el-tag>
      </template>
      <template slot-scope="{ row }" slot="action">
        <el-button @click="detail(row)" type="text">详细</el-button>
        <el-button @click="remove(row)" type="text">删除</el-button>
      </template>
    </table-page>

    <!-- 添加表单 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogShow"
      :close-on-click-modal="false"
      append-to-body>
      <el-form ref="form" :model="form" label-width="120px">
        <el-form-item label="任务名称">
          {{form.jobName}}
        </el-form-item>
        <el-form-item label="任务组名">
          <el-tag v-for="item in jobGroupStatusOptionList" v-if="form.jobGroup === item.dictValue" :type="item.listClass">
            {{item.dictLabel}}
          </el-tag>
        </el-form-item>
        <el-form-item label="调用目标字符串">
          {{form.invokeTarget}}
        </el-form-item>
        <el-form-item label="日志信息">
          {{form.jobMessage}}
        </el-form-item>
        <el-form-item label="执行状态">
          <el-tag v-for="item in jobStatusOptionList" v-if="form.jobStatus === item.dictValue" :type="item.listClass">
            {{item.dictLabel}}
          </el-tag>
        </el-form-item>
        <el-form-item label="异常信息" prop="exceptionInfo">
          {{form.exceptionInfo}}
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
  import TablePage from "_c/CommonForm/table-page";
  import {cleanJobLog, deleteBatchJobLogById, deleteJobLogById} from "_a/admin/job/job-log";
  import {ConfirmCustom, MessageError, MessageSuccess} from '_l/message'
  import {mapActions} from "vuex";

  export default {
    name: 'job-log-manage',
    components: {TablePage},
    props: {
      job: {
        required: true,
        type: Object
      }
    },
    data() {
      return {
        url:'monitor/jobLog/page',
        columns: [
          {label: '任务名称', prop: 'jobName'},
          {label: '任务组名', slot: 'jobGroup'},
          {label: '调用目标字符串', prop: 'invokeTarget'},
          {label: '日志信息', prop: 'jobMessage'},
          {label: '执行状态', slot: 'jobStatus'},
          {label: '异常信息', prop: 'exceptionInfo'},
          {label: '操作', slot: 'action'}
        ],
        queryParams: {},
        dialogShow: false,
        delBtnDisabled: true,
        dialogTitle: '添加调度日志',
        form: {},
        jobStatusOptionList: [],
        jobGroupStatusOptionList: [],
        executionDate:[]
      }
    },
    watch:{
      executionDate:function (val,oldVal) {
        this.queryParams.beginDate = val[0]
        this.queryParams.endDate = val[1]
      }
    },
    methods: {
      ...mapActions(['getDictListByType']),
      change(e) {
        this.$forceUpdate()
      },
      /**
       * 刷新
       */
      refresh() {
        this.$refs.tablePage.refresh();
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
      /**
       * 修改弹出框
       * @param row
       */
      detail(row) {
        this.form = {...row};
        this.dialogTitle = '调度日志详细';
        this.dialogShow = true;
      },
      /**
       * 点击每一行的checkbox
       */
      selectVal(ids) {
        this.ids = ids.map(val => val['id']);
        this.delBtnDisabled = !this.ids.length
      },
      /**
       * 批量删除
       */
      removeBatch() {
        ConfirmCustom({type: 'warning'}).then(() => {
          deleteBatchJobLogById(this.ids).then(res => {
            if (res.data) {
              MessageSuccess('del')
            } else {
              MessageError('del')
            }
            this.refresh()
          })
        })
      },
      /**
       * 行内删除
       * @param row
       */
      remove(row) {
        ConfirmCustom({type: 'warning'}).then(() => {
          deleteJobLogById(row.id).then(res => {
            if (res.data) {
              MessageSuccess('del')
            } else {
              MessageError('del')
            }
            // 刷新表格
            this.refresh()
          })
        })
      },
      clearLog(){
        cleanJobLog().then(res =>{
          if (res.data) {
            this.$message.success('操作成功');
          } else {
            this.$message.error('操作失败');
          }
          this.refresh()
        })
      }
    },
    created() {
      this.queryParams.jobName = this.job.jobName
      this.queryParams.jobGroup = this.job.jobGroup
    },
    mounted() {
      this.getDictListByType('job_status').then(res => {
        this.jobStatusOptionList = res
      })
      this.getDictListByType('job_group').then(res => {
        this.jobGroupStatusOptionList = res
      })
    }
  }
</script>

<style scoped lang="scss">

</style>
