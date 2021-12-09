<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form ref="queryParams" :model="queryParams" inline label-width="60px" label-position="left">
      <el-form-item label="名称">
        <el-input v-model="queryParams.name" clearable placeholder="请输入部门名称"/>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryParams.deptStatus" placeholder="请选择状态" style="width: 100%">
          <template v-for="item in statusOptionList">
            <el-option :value="item.dictValue" :label="item.dictLabel"/>
          </template>
        </el-select>
      </el-form-item>
      <el-form-item label-width="0px">
        <el-button icon="el-icon-search" type="primary" @click="refresh()">查询</el-button>
        <el-button icon="el-icon-refresh" type="primary" @click="reset()">重置</el-button>
        <el-button icon="el-icon-plus" type="primary" v-permission="['dept:save']" @click="addDept()">添加</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <table-page
      ref="tablePage"
      url="system/dept/getDeptTreeList"
      row-key="id"
      default-expand-all
      :check-box="false"
      :columns="columns"
      :query-params="queryParams">
      <template slot-scope="{ row }" slot="deptStatus">
        <el-tag v-for="item in statusOptionList" v-if="row.deptStatus === item.dictValue" :type="item.listClass">
          {{ item.dictLabel }}
        </el-tag>
      </template>
      <template slot-scope="{ row }" slot="action">
        <el-button @click="update(row)" v-permission="['dept:update']" type="text">修改</el-button>
        <el-button @click="remove(row)" v-permission="['dept:delete']" type="text">删除</el-button>
      </template>
    </table-page>

    <!-- 添加表单 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogShow"
      :close-on-click-modal="false"
      @close="handleReset('form')">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">

        <el-row>
          <el-col :span="12">
            <el-form-item label="上级部门" prop="deptId">
              <vue-treeselect v-model="form.parentId" :options="deptTree" placeholder="请选择上级部门" :normalizer="normalizer"
                              @select="treeSelect" @input="change($event)"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入部门名称" @input="change($event)"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="deptSort">
              <el-input-number v-model="form.deptSort" placeholder="请输入显示顺序" @input="change($event)"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门状态" prop="deptStatus">
              <el-radio-group v-model="form.deptStatus">
                <template v-for="item in statusOptionList">
                  <el-radio :label="item.dictValue">{{item.dictLabel}}</el-radio>
                </template>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" @input="change($event)"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" @input="change($event)"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 自定义按钮组 -->
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleReset('form')">取 消</el-button>
        <el-button type="primary" @click="handleSubmit('form')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import TablePage from "_c/CommonForm/table-page";
import {addDept, deleteDeptById, updateDept} from "_a/admin/dept/dept";
import {ConfirmCustom, MessageError, MessageSuccess} from '_l/message'
import {mapActions} from "vuex";
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import VueTreeselect from "@riophae/vue-treeselect";

export default {
  name: 'dept-manage',
  components: {TablePage, VueTreeselect},
  data() {
    return {
      columns: [
        {label: '部门名称', prop: 'name', width: '300', align: 'left'},
        {label: '排序', prop: 'deptSort'},
        {label: '联系电话', prop: 'phone'},
        {label: '邮箱', prop: 'email'},
        {label: '部门状态', slot: 'deptStatus'},
        {label: '操作', slot: 'action'}
      ],
      queryParams: {},
      dialogShow: false,
      dialogTitle: '添加部门',
      statusOptionList: [],
      form: {},
      rules: {
        name: [this.$ruler('部门名称')],
        deptSort: [this.$ruler('显示顺序')],
        deptStatus: [this.$ruler('部门状态')],
      },
      deptTree: [],
      normalizer(node) {
        return {
          id: node.id,
          label: node.name,
          children: node.children
        }
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
      this.queryParams = {}
      this.$nextTick(function () {
        this.refresh()
      })
    },
    /**
     * 添加弹出框
     */
    addDept() {
      this.dialogTitle = '添加部门';
      this.deptTree.length = 0;
      this.deptTree.unshift({id: 0, name: '顶级公司', parentIds: 0, children: this.$refs.tablePage.data})
      this.form = {
        deptStatus:'0',
        deptSort: 10
      };
      this.dialogShow = true;

    },
    /**
     * 修改弹出框
     * @param row
     */
    update(row) {
      this.form = {...row};
      this.deptTree.length = 0;
      this.deptTree.unshift({id: 0, name: '顶级公司', parentIds: 0, children: this.$refs.tablePage.data})
      this.dialogTitle = '修改部门';
      this.dialogShow = true;
    },
    /**
     * 行内删除
     * @param row
     */
    remove(row) {
      ConfirmCustom({type: 'warning'}).then(() => {
        deleteDeptById(row.id).then(res => {
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
          if (this.dialogTitle === '添加部门') {
            addDept(this.form).then(res => {
              if (res.data) {
                MessageSuccess('add')
              } else {
                MessageError('add')
              }
              this.cancelDialogAndRefresh()
            })
          } else {
            updateDept(this.form).then(res => {
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
    treeSelect(node, instanceId) {
      if (node.ancestors !== 0) {
        this.form.ancestors = node.ancestors + ',' + node.id
      } else {
        this.form.ancestors = node.id
      }
    }
  },
  mounted() {
    this.getDictListByType('dept_status').then(res => {
      this.statusOptionList = res
    })
  }
}
</script>

<style scoped lang="scss">

</style>
