<template>
  <div>
    <el-row>
      <el-col :span="24" style="text-align: center;padding: 20px">
        <el-image :src="processPicUrl" alt=""/>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24">
        <table-page
          ref="tablePage"
          :url="tableUrl"
          :columns="columns"
          :page-ing-type="false"
          :check-box="false">
          <template slot-scope="{ row }" slot="status">
            <el-tag v-if="row.status === 'success'" type="success">通过</el-tag>
            <el-tag v-if="row.status === 'reject'" type="danger">驳回</el-tag>
          </template>
        </table-page>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import TablePage from "@/components/CommonForm/table-page";

export default {
  name: "track-process",
  components: {TablePage},
  data() {
    return {
      processPicUrl: '',
      tableUrl: null,
      columns: [
        {label: '环节名称', prop: 'activityName'},
        {label: '开始时间', prop: 'startTime'},
        {label: '结束时间', prop: 'endTime'},
        {label: '任务历时', prop: 'duration'},
        {label: '操作人', prop: 'assignee'},
        {label: '审批状态', slot: 'status'},
        {label: '审批意见', prop: 'comment'}
      ],
    }
  },
  props: {
    processInstanceId: {
      type: String,
      required: true
    }
  },
  created() {
    this.tableUrl = `flow/task/getTaskInsList?processInstanceId=${this.processInstanceId}`;
    let time = new Date().getTime();
    this.processPicUrl = `${process.env.VUE_APP_PROXY_TARGET}flow/diagram/getTracePicByProcessInstanceId?processInstanceId=${this.processInstanceId}&time=${time}`;
  },
  mounted() {

  }
}
</script>

<style scoped>

</style>
