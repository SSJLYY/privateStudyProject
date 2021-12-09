<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form ref="queryParams" :model="queryParams" inline label-width="80px" label-position="left">
      <el-form-item label="请假时间">
        <el-date-picker
          v-model="leaveDate"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @input="change($event)">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="请假类型">
        <el-select v-model="queryParams.type" placeholder="请输入请假类型" style="width: 100%">
          <template v-for="item in leaveTypeOptionList">
            <el-option :value="item.dictValue" :label="item.dictLabel"/>
          </template>
        </el-select>
      </el-form-item>
      <el-form-item label-width="0px">
        <el-button icon="el-icon-search" type="primary" @click="refresh()">查询</el-button>
        <el-button icon="el-icon-refresh" type="primary" @click="reset()">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <table-page
      ref="tablePage"
      url="oa/leave/getMeLeavePageList"
      :columns="columns"
      :query-params="queryParams"
      :check-box="false">
      <template slot-scope="{ row }" slot="type">
        <el-tag v-for="item in leaveTypeOptionList" v-if="row.type === item.dictValue" :type="item.listClass">
          {{ item.dictLabel }}
        </el-tag>
      </template>
      <template slot-scope="{ row }" slot="status">
        <el-tag v-if="row.status === '1'" type="warning">未开始</el-tag>
        <el-tag v-if="row.status === '2'" type="primary">进行中</el-tag>
        <el-tag v-if="row.status === '3'" type="success">已完成</el-tag>
        <el-tag v-if="row.status === '4'" type="danger">驳回</el-tag>
      </template>
      <template slot-scope="{ row }" slot="action">
        <el-button @click="submit(row)" type="text" v-if="row.status === '1'">提交</el-button>
        <el-button @click="reSubmit(row)" type="text" v-if="row.status === '4'">重新提交</el-button>
        <el-button @click="track(row)" type="text" v-if="row.status !== '1'">追踪</el-button>
        <el-button @click="remove(row)" type="text">删除</el-button>
      </template>
    </table-page>

    <!-- 添加表单 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogShow"
      :close-on-click-modal="false"
      @close="handleReset('form')">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" disabled placeholder="请输入姓名" @input="change($event)"/>
        </el-form-item>
        <el-form-item label="请假时间" prop="beginDate">
          <el-date-picker
            v-model="form.leaveDate"
            format="yyyy-MM-dd HH:mm"
            value-format="yyyy-MM-dd HH:mm"
            type="datetimerange"
            range-separator="至"
            start-placeholder="请假开始日期"
            end-placeholder="请假结束日期"
            @input="change($event)">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="请假类型" prop="type">
          <el-select v-model="form.type" placeholder="请输入请假类型" style="width: 100%">
            <template v-for="item in leaveTypeOptionList">
              <el-option :value="item.dictValue" :label="item.dictLabel"/>
            </template>
          </el-select>
        </el-form-item>
        <el-form-item label="请假事由" prop="reason">
          <el-input v-model="form.reason" placeholder="请输入请假事由" @input="change($event)"/>
        </el-form-item>
      </el-form>
      <!-- 自定义按钮组 -->
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleReset('form')">取 消</el-button>
        <el-button type="primary" @click="handleSubmit('form')">确 定</el-button>
      </div>
    </el-dialog>


    <el-dialog
      title="追踪"
      :visible.sync="dialogTrackShow"
      width="80%"
      :close-on-click-modal="false">
      <track-process v-if="dialogTrackShow" :process-instance-id="processInstanceId"/>
    </el-dialog>
  </div>
</template>
<script>
import TablePage from "@/components/CommonForm/table-page";
import {deleteLeaveById, reSubmitLeave, submitLeave} from "_a/office/leave/leave";
import {ConfirmCustom, MessageError, MessageSuccess} from '@/libs/message'
import {mapActions} from "vuex";
import TrackProcess from "@/views/office/task/track-process";

export default {
  name: 'leave-manage',
  components: {TrackProcess, TablePage},
  data() {
    return {
      columns: [
        {label: '姓名', prop: 'name'},
        {label: '请假时间', prop: 'leaveDate'},
        {label: '开始时间', prop: 'beginDate'},
        {label: '结束时间', prop: 'endDate'},
        {label: '请假类型', slot: 'type'},
        {label: '请假事由', prop: 'reason'},
        {label: '状态', slot: 'status'},
        {label: '操作', slot: 'action'}
      ],
      queryParams: {},
      dialogShow: false,
      dialogTrackShow: false,
      dialogTitle: '添加',
      form: {},
      rules: {
        name: [this.$ruler('姓名')],
        beginDate: [this.$ruler('开始时间')],
        endDate: [this.$ruler('结束时间')],
        type: [this.$ruler('请假类型')],
        reason: [this.$ruler('请假事由')],
      },
      leaveDate: [],
      leaveTypeOptionList: [],
      processInstanceId: null
    }
  },
  watch: {
    leaveDate: function (newVal, oldVal) {
      if (newVal !== undefined && newVal !== null) {
        this.queryParams.beginDate = newVal[0]
        this.queryParams.endDate = newVal[1]
      }
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
      this.queryParams = {};
      this.$nextTick(function () {
        this.refresh()
      })
    },
    submit(row) {
      this.form = {...row};
      this.form.leaveDate = [row.beginDate, row.endDate]
      this.dialogTitle = '提交';
      this.$nextTick(function () {
        this.dialogShow = true;
      })
    },
    reSubmit(row) {
      this.form = {...row};
      this.form.leaveDate = [row.beginDate, row.endDate]
      this.dialogTitle = '重新提交';
      this.$nextTick(function () {
        this.dialogShow = true;
      })
    },
    /**
     * 行内删除
     * @param row
     */
    remove(row) {
      ConfirmCustom({type: 'warning'}).then(() => {
        deleteLeaveById(row.id).then(res => {
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
    track(row) {
      if (row.processInstanceId !== null) {
        this.processInstanceId = row.processInstanceId
        this.$nextTick(function () {
          this.dialogTrackShow = true;
        })
      }
    },
    /**
     * 重置表单
     * @param name
     */
    handleReset(name) {
      this.dialogShow = false;
      this.$refs[name].resetFields()
    },
    /**
     * 提交表单
     * @param name
     */
    handleSubmit(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          if (this.dialogTitle === '提交') {
            submitLeave(this.form).then(res => {
              if (res.data) {
                this.$message.success("提交成功")
              } else {
                this.$message.error("提交失败")
              }
              this.cancelDialogAndRefresh()
            })
          } else if (this.dialogTitle === '重新提交') {
            reSubmitLeave(this.form).then(res => {
              if (res.data) {
                this.$message.success("重新提交成功")
              } else {
                this.$message.error("重新提交失败")
              }
              this.cancelDialogAndRefresh()
            })
          }
        }
      })
    },
    /**
     * 关闭弹出框 和 刷新表格
     */
    cancelDialogAndRefresh() {
      // 清空表单
      this.handleReset('form');
      // 刷新表格
      this.refresh()
    },
  },
  mounted() {
    this.getDictListByType('leave_type').then(res => {
      this.leaveTypeOptionList = res
    })
  }
}
</script>

<style scoped lang="scss">

</style>
