<template>
  <div>
    <el-form ref="form" :model="form" disabled label-width="80px">
      <el-form-item label="姓名">
        <el-input v-model="form.name" disabled placeholder="请输入姓名" @input="change($event)"/>
      </el-form-item>
      <el-form-item label="请假时间">
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
      <el-form-item label="请假类型">
        <el-select v-model="form.type" placeholder="请输入请假类型" style="width: 100%">
          <template v-for="item in leaveTypeOptionList">
            <el-option :value="item.dictValue" :label="item.dictLabel"/>
          </template>
        </el-select>
      </el-form-item>
      <el-form-item label="请假事由">
        <el-input v-model="form.reason" placeholder="请输入请假事由" @input="change($event)"/>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {getLeaveById} from "@/api/office/leave/leave";
import {mapActions} from "vuex";

export default {
  name: "leave-form",
  data() {
    return {
      form: {},
      leaveTypeOptionList: [],
    }
  },
  props: {
    formId: {
      type: String,
      required: true
    }
  },
  methods: {
    ...mapActions(['getDictListByType']),
    change(e) {
      this.$forceUpdate()
    },
  },
  created() {
    getLeaveById(this.formId).then(res =>{
      if (res.data){
        this.form = res.data;
        this.form.leaveDate = [this.form.beginDate, this.form.endDate]
      }
    })
  },
  mounted() {
    this.getDictListByType('leave_type').then(res => {
      this.leaveTypeOptionList = res
    })
  }
}
</script>

<style scoped>

</style>
