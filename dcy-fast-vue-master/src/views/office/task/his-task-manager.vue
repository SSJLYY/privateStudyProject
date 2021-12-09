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
      url="/flow/task/getHisTaskList"
      :columns="columns"
      :query-params="queryParams"
      :check-box="false">
      <template slot-scope="{ row }" slot="action">
        <el-button @click="track(row)" type="text">追踪</el-button>
      </template>
    </table-page>

    <el-dialog
      title="追踪"
      :visible.sync="dialogTrackShow"
      width="80%"
      v-if="dialogTrackShow"
      :close-on-click-modal="false">
      <el-tabs value="form">
        <el-tab-pane label="表单" name="form">
          <component :is="formComponents[comName]" :form-id="taskObj.formId"/>
        </el-tab-pane>
        <el-tab-pane label="流程图" name="process">
          <track-process :process-instance-id="taskObj.processInstanceId"/>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
import TablePage from "@/components/CommonForm/table-page";
import TrackProcess from "@/views/office/task/track-process";
import formComps from "@/views/office/task/form";

export default {
  name: "his-task-manager",
  components: {TrackProcess, TablePage},
  data() {
    return {
      dialogTrackShow: false,
      dialogTaskShow: false,
      queryParams: {},
      columns: [
        {label: '流程定义名称', prop: 'processDefinitionName'},
        {label: '流程实例名称', prop: 'processInstanceName', showOverflowTooltip: true},
        {label: '发起人', prop: 'startUserName'},
        {label: '任务名称', prop: 'taskName'},
        {label: '发起时间', prop: 'startTime'},
        {label: '办理时间', prop: 'endTime'},
        {label: '任务历时', prop: 'duration'},
        {label: '操作', slot: 'action'}
      ],
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
     * 追踪
     * @param row
     */
    track(row) {
      this.taskObj = {...row}
      if (row.processInstanceId !== null) {
        this.comName = row.tableName + '-form';
        this.$nextTick(function () {
          this.dialogTrackShow = true;
        })
      }
    },
  }
}
</script>

<style scoped>

</style>
