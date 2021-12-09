<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form ref="queryParams" :model="queryParams" inline label-width="80px" label-position="left">
      <el-form-item label="角色名">
        <el-input v-model="queryParams.roleName" clearable placeholder="请输入角色名称"/>
      </el-form-item>
      <el-form-item label-width="0px">
        <el-button icon="el-icon-search" type="primary" @click="refresh()">查询</el-button>
        <el-button icon="el-icon-refresh" type="primary" @click="reset()">重置</el-button>
        <el-button icon="el-icon-plus" type="primary" v-permission="['role:save']" @click="addRole()">添加</el-button>
        <el-button icon="el-icon-delete" type="primary" v-permission="['role:delete']" @click="removeBatch()"
                   :disabled="delBtnDisabled">删除
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <table-page
      ref="tablePage"
      :url="tableUrl"
      :columns="columns"
      :query-params="queryParams"
      @table-select-val="selectVal">
      <template slot-scope="{ row, index }" slot="roleStatus">
        <el-tag v-for="item in statusOptionList" v-if="row.roleStatus === item.dictValue" :type="item.listClass">
          {{ item.dictLabel }}
        </el-tag>
      </template>
      <template slot-scope="{ row, index }" slot="dataScope">
        <el-tag v-for="item in dataScopeOptionList" v-if="row.dataScope === item.dictValue" :type="item.listClass">
          {{ item.dictLabel }}
        </el-tag>
      </template>
      <template slot-scope="{ row }" slot="action">
        <el-button @click="update(row)" v-permission="['role:update']" type="text">修改</el-button>
        <el-button @click="remove(row)" v-permission="['role:delete']" type="text">删除</el-button>
        <el-button @click="authModule(row)" v-permission="['role:auth:resource']" type="text">授权</el-button>
      </template>
    </table-page>

    <!-- 添加表单 -->
    <el-dialog
      :label="dialogTitle"
      :visible.sync="dialogShow"
      :close-on-click-modal="false"
      @close="handleReset('form')">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称"/>
        </el-form-item>
        <el-form-item label="权限字符" prop="roleKey">
          <el-input v-model="form.roleKey" placeholder="请输入角色权限字符串"/>
        </el-form-item>
        <el-form-item label="状态" prop="roleStatus">
          <el-radio-group v-model="form.roleStatus">
            <template v-for="item in statusOptionList">
              <el-radio :label="item.dictValue">{{ item.dictLabel }}</el-radio>
            </template>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="数据范围" prop="roleStatus">
          <el-select v-model="form.dataScope" placeholder="请输入数据范围" style="width: 100%">
            <template v-for="item in dataScopeOptionList">
              <el-option :value="item.dictValue" :label="item.dictLabel"/>
            </template>
          </el-select>
          <el-tree
            :data="deptTree"
            ref="deptTree"
            v-if="form.dataScope === '2'"
            show-checkbox
            default-expand-all
            node-key="id"
            :default-checked-keys="dataScopeDeptIds"
            :props="{children: 'children',label: 'name'}">
          </el-tree>
        </el-form-item>
      </el-form>
      <!-- 自定义按钮组 -->
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleReset('form')">取 消</el-button>
        <el-button type="primary" @click="handleSubmit('form')">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 授权模块 -->
    <el-dialog
      title="授权模块"
      v-if="dialogModuleShow"
      :visible.sync="dialogModuleShow"
      :close-on-click-modal="false"
      @close="dialogModuleShow = false">
      <el-tree
        :data="moduleData"
        node-key="id"
        ref="moduleTree"
        show-checkbox
        :default-checked-keys="defModuleIds"
        :props="{
          children: 'children',
          label: 'title'
        }"
      />
      <!-- 自定义按钮组 -->
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogModuleShow = false">取 消</el-button>
        <el-button type="primary" @click="saveAuthModule()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addRole,
  deleteBatchRoleById,
  deleteRoleById,
  getDataScopeDeptIdsByRoleId,
  saveAuthResource,
  updateRole
} from '_a/admin/role/role'
import {ConfirmCustom, MessageError, MessageSuccess} from '_l/message'
import {mapActions, mapGetters} from "vuex";
import {getResourceIdListByRoleId, getResourceTreeList} from "@/api/admin/resource/resource";
import TablePage from "_c/CommonForm/table-page";
import {getDeptTreeList} from "_a/admin/dept/dept";

export default {
  name: 'role-manage',
  components: {TablePage},
  data() {
    return {
      tableUrl: '/system/role/page',
      columns: [
        {label: '名称', prop: 'roleName'},
        {label: '权限字符', prop: 'roleKey'},
        {label: '状态', slot: 'roleStatus'},
        {label: '数据范围', slot: 'dataScope'},
        {label: '操作', slot: 'action'}
      ],
      queryParams: {},
      dialogShow: false,
      delBtnDisabled: true,
      dialogTitle: '添加角色',
      form: {},
      ids: [],
      rules: {
        roleName: [this.$ruler('角色名称')],
        roleKey: [this.$ruler('角色权限字符串'), {pattern: /^[A-Z_]{3,30}$/, message: '请输入大写字母或者下划线并且3-30个字符'}],
        roleStatus: [this.$ruler('角色状态')]
      },
      roleId: null,
      statusOptionList: [],
      dataScopeOptionList: [],
      dialogModuleShow: false,
      moduleData: [],
      defModuleIds: [],
      deptTree: [],
      dataScopeDeptIds: []
    }
  },
  computed: {
    ...mapGetters(["userId"]),
  },
  watch: {
    'form.dataScope': function (val) {
      if (val === '2') {
        this.$nextTick(function () {
          getDeptTreeList().then(res => {
            if (res.data) {
              this.deptTree = res.data
            }
          })
        })
      }
    }
  },
  methods: {
    ...mapActions(['getDictListByType']),
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
     * 添加弹出框
     */
    addRole() {
      this.form = {
        roleStatus: '0'
      };
      this.dialogTitle = '添加角色'
      this.dialogShow = true
    },
    /**
     * 修改弹出框
     * @param row
     */
    update(row) {
      this.form = {...row};
      this.dialogTitle = '修改角色'
      getDataScopeDeptIdsByRoleId(row.id).then(res => {
        if (res.data) {
          this.dataScopeDeptIds = res.data
          this.$nextTick(function () {
            this.dialogShow = true
          })
        }
      })

    },
    /**
     * 点击每一行的checkbox
     */
    selectVal(ids) {
      this.ids = ids.map(val => val['id'])
      this.delBtnDisabled = !this.ids.length
    },
    /**
     * 批量删除
     */
    removeBatch() {
      ConfirmCustom({type: 'warning'}).then(() => {
        deleteBatchRoleById(this.ids).then(res => {
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
        deleteRoleById(row.id).then(res => {
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
      this.dialogShow = false
      this.$refs[name].resetFields()
    },
    /**
     * 提交表单
     * @param name
     */
    handleSubmit(name) {
      if (this.form.dataScope === '2') {
        let deptIds = []
        this.$refs.deptTree.getCheckedNodes(false, true).forEach(e => {
          deptIds.push(e.id)
        })
        this.form.deptIds = deptIds;
      }
      this.$refs[name].validate((valid) => {
        if (valid) {
          if (this.dialogTitle === '添加角色') {
            addRole(this.form).then(res => {
              if (res.data) {
                MessageSuccess('add')
              } else {
                MessageError('add')
              }
              this.cancelDialogAndRefresh()
            })
          } else {
            updateRole(this.form).then(res => {
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
      this.handleReset('form')
      // 刷新表格
      this.refresh()
    },
    /**
     * 授权模块
     * @param row
     */
    authModule(row) {
      this.roleId = row.id
      let that = this;
      getResourceTreeList().then(res => {
        this.moduleData = res.data;
      }).then(() => {
        getResourceIdListByRoleId(this.roleId).then(res => {
          res.data.forEach((i,n) =>{
            let node = that.$refs.moduleTree.getNode(i);
            if(node.isLeaf){
              that.$refs.moduleTree.setChecked(node, true);
            }
          })
        })
      }).then(() => {
        this.$nextTick(function () {
          this.dialogModuleShow = true;
        })
      })
    },
    saveAuthModule() {
      // 获取选中及半选节点
      let resIds = []
      this.$refs.moduleTree.getCheckedNodes(false, true).forEach(e => {
        resIds.push(e.id)
      })
      let roleResourceDto = {
        userId: this.userId,
        roleId: this.roleId,
        resIds: resIds
      }
      saveAuthResource(roleResourceDto).then(res => {
        if (res.data) {
          MessageSuccess('upd')
        } else {
          MessageError('upd')
        }
        this.dialogModuleShow = false;
        // 刷新表格
        this.refresh()
      })
    },
  },
  mounted() {
    this.getDictListByType('role_status').then(res => {
      this.statusOptionList = res
    })
    this.getDictListByType('data_scope').then(res => {
      this.dataScopeOptionList = res
    })
  }
}
</script>

<style scoped lang="scss">

</style>
