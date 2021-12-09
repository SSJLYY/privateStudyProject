<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form ref="queryParams" :model="queryParams" inline label-width="120px" label-position="left">
      <el-form-item label="流程定义名称">
        <el-input v-model="queryParams.processDefinitionName" clearable placeholder="请输入流程定义名称"/>
      </el-form-item>
      <el-form-item label="任务名称">
        <el-input v-model="queryParams.taskName" clearable placeholder="请输入任务名称"/>
      </el-form-item>
      <el-form-item label-width="0px">
        <el-button icon="el-icon-search" type="primary" @click="refresh()">查询</el-button>
        <el-button icon="el-icon-refresh" type="primary" @click="reset()">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <table-page
      ref="tablePage"
      url="/flow/task/getRunTaskList"
      :columns="columns"
      :query-params="queryParams"
      :check-box="false">
      <template slot-scope="{ row }" slot="action">
        <el-button @click="handleTask(row)" type="text">办理任务</el-button>
      </template>
    </table-page>

    <el-dialog
      title="办理任务"
      :visible.sync="dialogTaskShow"
      v-if="dialogTaskShow"
      width="80%"
      :close-on-click-modal="false">
      <el-tabs value="form">
        <el-tab-pane label="表单" name="form">
          <component :is="formComponents[comName]" :form-id="taskObj.formId"/>
        </el-tab-pane>
        <el-tab-pane label="流程图" name="process">
          <track-process :process-instance-id="taskObj.processInstanceId"/>
        </el-tab-pane>
      </el-tabs>
      <el-divider></el-divider>
      <el-row>
        <el-col :span="24" style="text-align: center;padding: 20px">
          <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="审核批注" prop="comment">
              <el-input v-model="form.comment" type="textarea" :rows="3" placeholder="请输入审核批注" @input="change($event)"/>
            </el-form-item>
            <el-form-item>
              <el-button icon="el-icon-circle-check" type="primary" @click="handleStatus(true)">通过</el-button>
              <el-button icon="el-icon-circle-close" type="danger" @click="handleStatus(false)">驳回</el-button>
              <el-button icon="el-icon-close" @click="closeTaskDialog()">关闭</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-dialog>

  </div>
</template>

<script>
import TablePage from "@/components/CommonForm/table-page";
import TrackProcess from "@/views/office/task/track-process";
import {completeTaskAndCommentAndSetVar} from "@/api/office/task/task";
import formComps from "@/views/office/task/form";

export default {
  name: "run-task-manager",
  components: {TrackProcess, TablePage},
  data() {
    return {
      dialogTaskShow: false,
      queryParams: {},
      columns: [
        {label: '流程定义名称', prop: 'processDefinitionName'},
        {label: '流程实例名称', prop: 'processInstanceName', showOverflowTooltip: true},
        {label: '发起人', prop: 'startUserName'},
        {label: '发起时间', prop: 'startTime'},
        {label: '接收时间', prop: 'claimTime'},
        {label: '到期时间', prop: 'dueDate'},
        {label: '任务名称', prop: 'taskName'},
        {label: '当前操作人', prop: 'curTaskUserName'},
        {label: '优先级', prop: 'priority'},
        {label: '操作', slot: 'action'}
      ],
      form: {},
      rules: {
        comment: [this.$ruler('请输入批注')],
      },
      taskObj: null,
      comName: null,
      formComponents: formComps
    }
  },
  methods: {
    change(e) {
      this.$forceUpdate()
    },
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
    /**
     * 办理任务
     * @param row
     */
    handleTask(row) {
      this.taskObj = {...row}
      if (row.processInstanceId !== null) {
        this.comName = row.tableName + '-form';
        this.$nextTick(function () {
          this.dialogTaskShow = true;
        })
      }
    },
    /**
     * 关闭办理任务窗口
     */
    closeTaskDialog() {
      this.dialogTaskShow = false;
      this.$refs['form'].resetFields()
    },
    /**
     * 办理任务
     * @param status true通过；false驳回
     */
    handleStatus(status) {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          completeTaskAndCommentAndSetVar(this.taskObj.taskId, this.form.comment, status).then(res => {
            if (res.data) {
              this.$message.success("办理成功")
            } else {
              this.$message.error("办理失败")
            }
            // 清空表单
            this.closeTaskDialog()
            // 刷新表格
            this.refresh()
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
