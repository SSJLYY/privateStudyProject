<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form ref="queryParams" :model="queryParams" inline label-width="80px" label-position="left">
      <el-form-item label="任务名称">
        <el-input v-model="queryParams.jobName" clearable placeholder="请输入任务名称"/>
      </el-form-item>
      <el-form-item label="任务组名">
        <el-select v-model="queryParams.jobGroup" placeholder="请选择任务组名" style="width: 100%">
          <template v-for="item in jobGroupStatusOptionList">
            <el-option :value="item.dictValue" :label="item.dictLabel"/>
          </template>
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryParams.jobStatus" placeholder="请选择状态" style="width: 100%">
          <template v-for="item in jobStatusOptionList">
            <el-option :value="item.dictValue" :label="item.dictLabel"/>
          </template>
        </el-select>
      </el-form-item>
      <el-form-item label-width="0px">
        <el-button icon="el-icon-search" type="primary" @click="refresh()">查询</el-button>
        <el-button icon="el-icon-refresh" type="primary" @click="reset()">重置</el-button>
        <el-button icon="el-icon-plus" type="primary" v-permission="['job:save']" @click="addJob()">添加</el-button>
        <el-button icon="el-icon-delete" type="primary" v-permission="['job:delete']" @click="removeBatch()" :disabled="delBtnDisabled">删除</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <table-page
      ref="tablePage"
      url="monitor/job/page"
      :columns="columns"
      :query-params="queryParams"
      @table-select-val="selectVal">
      <template slot-scope="{ row }" slot="jobGroup">
        <el-tag v-for="item in jobGroupStatusOptionList" v-if="row.jobGroup === item.dictValue" :type="item.listClass">
          {{item.dictLabel}}
        </el-tag>
      </template>
      <template slot-scope="{ row }" slot="jobStatus">
        <el-switch
          v-model="row.jobStatus"
          active-value="0"
          inactive-value="1"
          @change="changeStatus(row)"
          active-color="#13ce66"
          inactive-color="#ff4949">
        </el-switch>
      </template>
      <template slot-scope="{ row }" slot="action">
        <el-button @click="runOne(row)" v-permission="['job:run']" type="text">执行一次</el-button>
        <el-button @click="taskLog(row)" v-permission="['job:task:log']" type="text">调度日志</el-button>
        <el-button @click="update(row)" v-permission="['job:update']" type="text">修改</el-button>
        <el-button @click="remove(row)" v-permission="['job:delete']" type="text">删除</el-button>
      </template>
    </table-page>

    <!-- 添加表单 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogShow"
      :close-on-click-modal="false"
      @close="handleReset('form')">
      <el-form ref="form" :model="form" :rules="rules" label-width="130px">
        <el-form-item label="任务名称" prop="jobName">
          <el-input v-model="form.jobName" placeholder="请输入任务名称" @input="change($event)"/>
        </el-form-item>
        <el-form-item label="任务组名" prop="jobGroup">
          <el-select v-model="form.jobGroup" placeholder="请选择任务组名" @input="change($event)" style="width: 100% ">
            <template v-for="item in jobGroupStatusOptionList">
              <el-option :value="item.dictValue" :label="item.dictLabel"/>
            </template>
          </el-select>
        </el-form-item>
        <el-form-item label="调用目标字符串" prop="invokeTarget">
          <el-input v-model="form.invokeTarget" placeholder="请输入调用目标字符串" @input="change($event)"/>
          <el-row>
            <el-col :span="24">
              Bean调用示例：ryTask.ryParams('ry')
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              Class类调用示例：com.dcy.quartz.task.RyTask.ryParams('ry')
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              参数说明：支持字符串，布尔类型，长整型，浮点型，整型
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="cron表达式" prop="cronExpression">
          <el-input v-model="form.cronExpression" placeholder="请输入cron表达式" @input="change($event)"/>
        </el-form-item>
        <el-form-item label="执行策略" prop="misfirePolicy">
          <el-radio-group v-model="form.misfirePolicy" placeholder="请选择执行策略" style="width: 100%">
            <template v-for="item in misfirePolicyOptionList">
              <el-radio :label="item.dictValue">{{item.dictLabel}}</el-radio>
            </template>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="并发执行" prop="concurrent">
          <el-radio-group v-model="form.concurrent" placeholder="请选择并发执行" style="width: 100%">
            <template v-for="item in concurrentOptionList">
              <el-radio :label="item.dictValue">{{item.dictLabel}}</el-radio>
            </template>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="jobStatus">
          <el-radio-group v-model="form.jobStatus" placeholder="请选择状态" style="width: 100%">
            <template v-for="item in jobStatusOptionList">
              <el-radio :label="item.dictValue">{{item.dictLabel}}</el-radio>
            </template>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <!-- 自定义按钮组 -->
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleReset('form')">取 消</el-button>
        <el-button type="primary" @click="handleSubmit('form')">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 调度日志 -->
    <el-dialog
      title="调度日志"
      width="90%"
      :visible.sync="dialogTaskLogShow"
      :close-on-click-modal="false">
      <job-log-manage :job="cunRow" v-if="dialogTaskLogShow"/>
      <!-- 自定义按钮组 -->
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogTaskLogShow = !dialogTaskLogShow">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import TablePage from "_c/CommonForm/table-page";
  import {
    addJob, changeStatusJob,
    checkCronExpressionIsValid,
    deleteBatchJobById,
    deleteJobById,
    runJob,
    updateJob
  } from "_a/admin/job/job";
  import {ConfirmCustom, MessageError, MessageSuccess} from '_l/message'
  import {mapActions} from "vuex";
  import JobLogManage from "@/views/admin/job/job-log-manage";

  export default {
    name: 'job-manage',
    components: {JobLogManage, TablePage},
    data() {
      const validateCronCheck = (rule, value, callback) => {
        if (value !== '') {
          checkCronExpressionIsValid(value).then(res => {
            if (!res.data) {
              callback(new Error('cron表达式输入不对，请重新输入'))
            } else {
              callback()
            }
          })
        } else {
          callback()
        }
      };
      return {
        columns: [
          {label: '任务名称', prop: 'jobName'},
          {label: '任务组名', slot: 'jobGroup'},
          {label: '调用目标字符串', prop: 'invokeTarget'},
          {label: 'cron表达式', prop: 'cronExpression'},
          {label: '状态', slot: 'jobStatus'},
          {label: '操作', slot: 'action'}
        ],
        queryParams: {},
        dialogShow: false,
        dialogTaskLogShow: false,
        delBtnDisabled: true,
        dialogTitle: '添加任务',
        form: {},
        rules: {
          jobName: [this.$ruler('任务名称')],
          jobGroup: [this.$ruler('任务组名')],
          invokeTarget: [this.$ruler('调用目标字符串')],
          cronExpression: [
            this.$ruler('cron表达式'),
            {validator: validateCronCheck, trigger: 'blur'}],
        },
        misfirePolicyOptionList: [],
        concurrentOptionList: [],
        jobStatusOptionList: [],
        jobGroupStatusOptionList: [],
        cunRow:null
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
       * 添加弹出框
       */
      addJob() {
        this.form = {
          misfirePolicy: '3',
          concurrent: '1',
          jobStatus: '0'
        };
        this.dialogTitle = '添加任务';
        this.dialogShow = true;
      },
      /**
       * 修改弹出框
       * @param row
       */
      update(row) {
        this.form = {...row};
        this.dialogTitle = '修改任务';
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
          deleteBatchJobById(this.ids).then(res => {
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
          deleteJobById(row.id).then(res => {
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
            if (this.dialogTitle === '添加任务') {
              addJob(this.form).then(res => {
                if (res.data) {
                  MessageSuccess('add')
                } else {
                  MessageError('add')
                }
                this.cancelDialogAndRefresh()
              })
            } else {
              updateJob(this.form).then(res => {
                if (res.data) {
                  MessageSuccess('upd')
                } else {
                  MessageError('upd')
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
      /**
       * 执行一次
       * @param row
       */
      runOne(row) {
        this.$confirm('确认要立即执行一次任务吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          runJob(row.id).then(res => {
            if (res.data) {
              this.$message.success('操作成功');
            } else {
              this.$message.error('操作失败');
            }
          })
        })
      },
      /**
       * 切换状态
       * @param row
       */
      changeStatus(row) {
        let obj = {
          id: row.id,
          jobGroup: row.jobGroup,
          jobStatus: row.jobStatus
        };
        let typeName = row.jobStatus === '0' ? '启用' : '禁用';
        this.$confirm('确认要' + typeName + '任务吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          changeStatusJob(obj).then(res => {
            if (res.data) {
              this.$message.success(typeName + '成功');
            } else {
              this.$message.error(typeName + '失败');
            }
          })
        }).catch(() => {
          row.jobStatus = !row.jobStatus
        })
      },
      /**
       * 获取调度日志
       * @param row
       */
      taskLog(row) {
        this.cunRow = row;
        this.$nextTick(function () {
          this.dialogTaskLogShow = true;
        })
      }
    },
    mounted() {
      this.getDictListByType('misfire_policy_type').then(res => {
        this.misfirePolicyOptionList = res
      })
      this.getDictListByType('concurrent_type').then(res => {
        this.concurrentOptionList = res
      })
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
