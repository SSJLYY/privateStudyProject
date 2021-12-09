<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="请假时间" prop="leaveDate">
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
        <el-input v-model="form.reason" type="textarea" :rows="3" placeholder="请输入请假事由" @input="change($event)"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSave('form')">保存</el-button>
        <el-button type="primary" @click="handleSubmit('form')">保存并提交</el-button>
        <el-button @click="handleReset('form')">取 消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import TablePage from "@/components/CommonForm/table-page";
import {createAndSubmitLeave, saveLeave} from "_a/office/leave/leave";
import {mapActions} from "vuex";

export default {
  name: 'leave-apply',
  components: {TablePage},
  data() {
    return {
      form: {},
      rules: {
        leaveDate: [
          {
            type: 'array', required: true, message: '请选择开始时间和结束时间', trigger: 'change',
            fields: {
              0: {type: 'string', required: true, message: '请选择开始日期'},
              1: {type: 'string', required: true, message: '请选择结束日期'}
            }
          }],
        type: [this.$ruler('请假类型')],
        reason: [this.$ruler('请假事由')],
      },
      leaveTypeOptionList: [],

    }
  },
  watch: {
    'form.leaveDate': function (newVal, oldVal) {
      if (newVal !== undefined && newVal !== null) {
        this.form.beginDate = newVal[0]
        this.form.endDate = newVal[1]
      }
    }
  },
  methods: {
    ...mapActions(['getDictListByType']),
    change(e) {
      this.$forceUpdate()
    },
    /**
     * 重置表单
     * @param name
     */
    handleReset(name) {
      this.dialogShow = false;
      this.$refs[name].resetFields()
    },
    handleSave(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          delete this.form.leaveDate
          saveLeave(this.form).then(res => {
            if (res.data) {
              this.$message.success("保存成功")
            } else {
              this.$message.error("保存失败")
            }
            // 清空表单
            this.handleReset('form');
          })
        }
      })
    },
    /**
     * 提交表单
     * @param name
     */
    handleSubmit(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          delete this.form.leaveDate
          createAndSubmitLeave(this.form).then(res => {
            if (res.data) {
              this.$message.success("提交成功")
            } else {
              this.$message.error("提交失败")
            }
            // 清空表单
            this.handleReset('form');
          })
        }
      })
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
