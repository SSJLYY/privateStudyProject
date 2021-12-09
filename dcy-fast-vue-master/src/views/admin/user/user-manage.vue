<template>
  <div class="app-container">
    <el-row>
      <el-col :span="4">
        <el-card shadow="never">
          <div slot="header" style="display: flex;justify-content: space-between;align-items: center">
            <span>组织机构</span>
            <div>
              <el-button size="mini" icon="el-icon-refresh" circle style="float: right;"
                         @click="getDeptTreeList()"></el-button>
              <el-button size="mini" icon="el-icon-edit" circle style="float: right;" @click="toDeptView()"></el-button>
            </div>
          </div>
          <el-tree
            :data="deptTree"
            node-key="id"
            default-expand-all
            :expand-on-click-node="false"
            :props="{children: 'children',label: 'name'}"
            @node-click="handleNodeClick"></el-tree>
        </el-card>

      </el-col>
      <el-col :span="20" style="padding-left: 20px">
        <!-- 查询条件 -->
        <el-form ref="queryParams" :model="queryParams" inline label-width="60px" label-position="left">
          <el-form-item label="用户名">
            <el-input type="text" v-model="queryParams.username" clearable placeholder="请输入用户名"/>
          </el-form-item>
          <el-form-item label="昵称">
            <el-input type="text" v-model="queryParams.nickName" clearable placeholder="请输入昵称"/>
          </el-form-item>
          <el-form-item label-width="0px">
            <el-button icon="el-icon-search" type="primary" @click="refresh()">查询</el-button>
            <el-button icon="el-icon-refresh" type="primary" @click="reset()">重置</el-button>
            <el-button icon="el-icon-plus" type="primary" v-permission="['user:save']" @click="addUser()">添加</el-button>
            <el-button icon="el-icon-delete" type="primary" v-permission="['user:delete']" @click="removeBatch()"
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
          @table-select-val="selectVal"
        >
          <template slot-scope="{ row }" slot="sex">
            <el-tag v-for="item in sexOptionList" v-if="row.sex === item.dictValue" :type="item.listClass">
              {{ item.dictLabel }}
            </el-tag>
          </template>
          <template slot-scope="{ row }" slot="userStatus">
            <el-tag v-for="item in statusOptionList" v-if="row.userStatus === item.dictValue" :type="item.listClass">
              {{ item.dictLabel }}
            </el-tag>
          </template>
          <template slot-scope="{ row }" slot="action">
            <el-button @click="update(row)" v-permission="['user:update']" type="text">修改</el-button>
            <el-button @click="remove(row)" v-permission="['user:delete']" type="text">删除</el-button>
            <el-button @click="resetPassword(row)" v-permission="['user:reset:pass']" type="text">重置密码</el-button>
            <el-button @click="authRole(row)" v-permission="['user:auth:role']" type="text">授权角色</el-button>
          </template>
        </table-page>
      </el-col>
    </el-row>

    <el-dialog :title="dialogTitle" v-if="dialogShow" :visible.sync="dialogShow" :close-on-click-modal="false"
               @close="handleReset('form')">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="部门" prop="deptId">
          <vue-treeselect v-model="form.deptId" :options="deptTree" placeholder="请选择部门" :normalizer="normalizer"
                          @input="change($event)"/>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" placeholder="请输入用户名"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="昵称" prop="nickName">
              <el-input v-model="form.nickName" placeholder="请输入昵称"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="密码" prop="password" v-if="dialogTitle === '添加用户'">
              <el-input v-model="form.password" placeholder="请输入密码" show-password @input="change($event)"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="确认密码" prop="passwordCheck" v-if="dialogTitle === '添加用户'">
              <el-input v-model="form.passwordCheck" placeholder="请输入确认密码" show-password @input="change($event)"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="性别" prop="sex">
              <el-radio-group v-model="form.sex">
                <template v-for="item in sexOptionList">
                  <el-radio :label="item.dictValue">{{ item.dictLabel }}</el-radio>
                </template>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="userStatus">
              <el-radio-group v-model="form.userStatus">
                <template v-for="item in statusOptionList">
                  <el-radio :label="item.dictValue">{{ item.dictLabel }}</el-radio>
                </template>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phoneNumber">
              <el-input v-model="form.phoneNumber" placeholder="请输入手机号"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="岗位" prop="postIds">
          <el-select v-model="form.postIds" @input="change($event)" multiple placeholder="请输入岗位" style="width: 100%">
            <el-option
              v-for="item in postList"
              :key="item.id"
              :label="item.postName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleReset('form')">取 消</el-button>
        <el-button type="primary" @click="handleSubmit('form')">确 定</el-button>
      </div>
    </el-dialog>


    <el-dialog
      title="授权角色"
      :visible.sync="dialogAuthRoleShow"
      :close-on-click-modal="false">
      <el-transfer
        v-if="dialogAuthRoleShow"
        filterable
        :filter-method="filterMethod"
        v-model="targetKeys"
        :titles="['角色列表','已授权角色']"
        :data="roleList">
      </el-transfer>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelAuthRoleDialog()">取 消</el-button>
        <el-button type="primary" @click="saveAuthRole()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addUserInfo,
  deleteBatchUserInfoById,
  deleteUserInfoById,
  getAuthRoleListByUserId,
  getPostListByUserId,
  getUserInfoByUsername,
  resetPassword,
  saveAuthRole,
  updateUserInfo
} from '_a/admin/user/user'
import {ConfirmCustom, MessageError, MessageSuccess} from '_l/message'
import {getRoleAllList} from '_a/admin/role/role'
import {mapActions} from "vuex";
import TablePage from "_c/CommonForm/table-page";
import {getDeptTreeList} from "_a/admin/dept/dept";
import {getPostListAll} from "_a/admin/post/post";
import VueTreeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'user-manage',
  components: {TablePage, VueTreeselect},
  data() {
    const validateUsernameCount = (rule, value, callback) => {
      if (value !== '') {
        getUserInfoByUsername(this.form.username).then(res => {
          if (res.data !== null) {
            if (this.form.userId === res.data.userId) {
              callback()
            } else {
              callback(new Error('用户名重复，请重新输入'))
            }
          } else {
            callback()
          }
        })
      } else {
        callback()
      }
    }
    const validatePassCheck = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入确认密码'))
      } else if (value !== this.form.password) {
        callback(new Error('两次密码不一致，请重新输入'))
      } else {
        callback()
      }
    }
    return {
      tableUrl: '/system/user/pageList',
      columns: [
        {label: '部门', prop: 'deptName'},
        {label: '用户名', prop: 'username', sortable: 'custom'},
        {label: '昵称', prop: 'nickName'},
        {label: '手机号码', prop: 'phoneNumber'},
        {label: '性别', slot: 'sex'},
        {label: '状态', slot: 'userStatus'},
        {label: '操作', slot: 'action', width: 400}
      ],
      queryParams: {},
      dialogShow: false,
      dialogAuthRoleShow: false,
      delBtnDisabled: true,
      dialogTitle: '添加用户',
      form: {},
      ids: [],
      rules: {
        deptId: [
          this.$ruler('部门')
        ],
        username: [
          this.$ruler('用户名'),
          {type: 'string', max: 16, min: 6, message: '请输入6-16个字符', trigger: 'blur'},
          {validator: validateUsernameCount, trigger: 'blur'}
        ],
        password: [
          this.$ruler('密码'),
          {type: 'string', max: 16, min: 6, message: '请输入6-16个字符', trigger: 'blur'}
        ],
        passwordCheck: [
          this.$ruler('确认密码'),
          {validator: validatePassCheck, trigger: 'blur'}
        ],
        nickName: [
          this.$ruler('昵称')
        ],
        phoneNumber: [
          this.$ruler('手机号')
        ]
      },
      sexOptionList: [],
      statusOptionList: [],
      // 授权角色使用
      userId: null,
      roleList: [],
      targetKeys: [],
      // 组织机构数据
      deptTree: [],
      // 岗位数据
      postList: [],
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
    addUser() {
      this.form = {
        password: null,
        userType: 1,
        userStatus: '0',
      };
      this.dialogTitle = '添加用户'
      this.dialogShow = true
    },
    /**
     * 修改弹出框
     * @param row
     */
    update(row) {
      this.form = {...row}
      this.dialogTitle = '修改用户'
      getPostListByUserId(row.id).then(res => {
        if (res.data) {
          this.form.postIds = res.data;
        }
        this.$nextTick(function () {
          this.dialogShow = true
        })
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
        deleteBatchUserInfoById(this.ids).then(res => {
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
        deleteUserInfoById(row.id).then(res => {
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
     * 重置密码
     * @param row
     */
    resetPassword(row) {
      ConfirmCustom({type: 'warning'}, '确定重置密码123456吗?').then(() => {
        row.password = '123456'
        resetPassword(row).then(res => {
          if (res.data) {
            MessageSuccess('upd')
          } else {
            MessageError('upd')
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
      this.$refs[name].validate((valid) => {
        if (valid) {
          if (this.dialogTitle === '添加用户') {
            addUserInfo(this.form).then(res => {
              if (res.data) {
                MessageSuccess('add')
              } else {
                MessageError('add')
              }
              this.cancelDialogAndRefresh()
            })
          } else {
            updateUserInfo(this.form).then(res => {
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
     * 关闭授权角色弹出框
     */
    cancelAuthRoleDialog() {
      this.dialogAuthRoleShow = false
    },
    filterMethod(query, item) {
      return item.label.indexOf(query) > -1
    },
    /**
     * 授权角色
     * @param row
     */
    authRole(row) {
      this.userId = row.id
      getAuthRoleListByUserId(this.userId).then(res => {
        this.targetKeys = res.data.map(e => e.id)
      }).then(() => {
        getRoleAllList().then(res => {
          if (res.data) {
            let roleList = []
            res.data.forEach(e => {
              let role = {
                key: e.id,
                label: e.roleName,
                disabled: e.roleStatus === '1'
              }
              roleList.push(role)
            })
            this.roleList = roleList
          }
        })
      }).then(() => {
        this.$nextTick(function () {
          this.dialogAuthRoleShow = true
        })
      })
    },
    /**
     * 保存授权角色
     */
    saveAuthRole() {
      let userInfoRoleDTO = {
        userId: this.userId,
        roleIds: this.targetKeys
      }
      saveAuthRole(userInfoRoleDTO).then(res => {
        if (res.data) {
          MessageSuccess('save')
        } else {
          MessageError('save')
        }
        this.cancelAuthRoleDialog()
        // 刷新表格
        this.refresh()
      })
    },
    getDeptTreeList() {
      getDeptTreeList().then(res => {
        if (res.data) {
          this.deptTree = res.data
        }
      })
    },
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.$nextTick(function () {
        this.refresh();
      })
    },
    toDeptView() {
      this.$router.push('/admin/dept-manage')
    },
    getPostList() {
      getPostListAll().then(res => {
        if (res.data) {
          this.postList = res.data;
        }
      })
    }
  },
  mounted() {
    this.getDeptTreeList()
    this.getPostList();
    this.getDictListByType('sex').then(res => {
      this.sexOptionList = res
    })
    this.getDictListByType('user_status').then(res => {
      this.statusOptionList = res
    })
  }
}
</script>

<style scoped lang="scss">

</style>
