<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form ref="queryParams" :model="queryParams" inline label-width="80px" label-position="left">
      <el-form-item label="操作人员">
        <el-input v-model="queryParams.operName" clearable placeholder="请输入操作人员"/>
      </el-form-item>
      <el-form-item label="请求地址">
        <el-input v-model="queryParams.url" clearable placeholder="请输入请求地址"/>
      </el-form-item>
      <el-form-item label="ip地址">
        <el-input v-model="queryParams.ip" clearable placeholder="请输入ip地址"/>
      </el-form-item>
      <el-form-item label="业务模块">
        <el-input v-model="queryParams.businessName" clearable placeholder="请输入业务模块"/>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryParams.logStatus" clearable placeholder="请输入状态" style="width: 100%">
          <el-option value="0" label="正常"/>
          <el-option value="1" label="异常"/>
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
      url="system/log/page"
      :columns="columns"
      :check-box="false"
      :query-params="queryParams">
      <template slot-scope="{ row }" slot="error">
        <el-button v-if="row.logStatus === '1' " @click="showError(row.error)" type="text">异常信息</el-button>
      </template>
    </table-page>

    <el-dialog
      title="异常信息"
      :visible.sync="dialogErrorShow"
      :close-on-click-modal="false">
      <span style="line-height: 28px">{{error}}</span>
    </el-dialog>
  </div>
</template>

<script>
import TablePage from "_c/CommonForm/table-page";

export default {
  name: 'log-manage',
  components: {TablePage},
  data() {
    return {
      dialogErrorShow:false,
      columns: [
        {label: '操作人员', prop: 'operName'},
        {label: '请求地址', prop: 'url'},
        {label: 'ip地址', prop: 'ip'},
        {label: '业务模块名称', prop: 'businessName'},
        {label: '请求参数', prop: 'operParam', showOverflowTooltip: true},
        {label: '方法名', prop: 'method', showOverflowTooltip: true},
        {label: '返回结果', prop: 'result', showOverflowTooltip: true},
        {label: '错误信息', slot: 'error'}
      ],
      queryParams: {},
      error:null
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
     * 重置表单
     * @param name
     */
    handleReset(name) {
      this.$refs[name].resetFields()
    },
    showError(error){
      this.dialogErrorShow = true;
      this.error = error;
    }
  },
  mounted() {

  }
}
</script>

<style scoped lang="scss">

</style>
