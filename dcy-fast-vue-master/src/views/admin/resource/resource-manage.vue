<template>
  <div class="app-container">

    <el-row style="margin-bottom: 20px">
      <el-col :span="24">
        <el-button type="primary" @click="toggleRowExpansion(!expansion)" icon="el-icon-arrow-down">展开/折叠
        </el-button>
        <el-button icon="el-icon-plus" type="primary" v-permission="['resource:add']" @click="addResource()">添加
        </el-button>
      </el-col>
    </el-row>

    <!-- 表格 -->
    <table-page
      ref="tablePage"
      :url="tableUrl"
      row-key="id"
      :page-ing-type="false"
      :check-box="false"
      :columns="columns">
      <template slot="type" slot-scope="{row}">
        <el-tag type="primary" v-if="row.type === '1'">目录</el-tag>
        <el-tag type="warning" v-if="row.type === '2'">菜单</el-tag>
        <el-tag type="info" v-if="row.type === '3'">按钮</el-tag>
      </template>
      <template slot="resStatus" slot-scope="{row}">
        <el-tag type="success" v-if="row.resStatus === '0'">正常</el-tag>
        <el-tag type="error" v-if="row.resStatus === '1'">禁用</el-tag>
      </template>
      <template slot-scope="{ row }" slot="action">
        <el-button @click="update(row)" v-permission="['resource:update']" type="text">修改</el-button>
        <el-button @click="remove(row)" v-permission="['resource:delete']"
                   v-if="row.children === null || row.children.length === 0" type="text">删除
        </el-button>
      </template>
    </table-page>

    <!-- 添加表单 -->
    <el-dialog
      :title="dialogTitle"
      v-if="dialogShow"
      :visible.sync="dialogShow"
      :close-on-click-modal="false"
      @close="handleReset('form')">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="菜单类型" prop="type" @input="change($event)">
              <el-radio-group v-model="form.type">
                <template v-for="item in resourceTypeOptionList">
                  <el-radio-button :label="item.dictValue">{{ item.dictLabel }}</el-radio-button>
                </template>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入标题" @input="change($event)"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="上级类目" prop="parentId">
              <vue-treeselect v-model="form.parentId" :options="menuList" placeholder="请选择上级类目" :normalizer="normalizer"
                              @select="treeSelect"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序" prop="resSort">
              <el-input-number v-model="form.resSort" placeholder="请输入排序" :precision="2" :step="0.1"
                               @input="change($event)"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row v-if="form.menuExtFlag ==='2' || form.type === '3'">
          <el-col :span="12">
            <el-form-item label="权限标识" prop="permission">
              <el-input v-model="form.permission" placeholder="请输入权限标识" @input="change($event)"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="后端url路径地址" prop="resPath">
              <el-input v-model="form.resPath" placeholder="请输入后端url路径地址" @input="change($event)"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="请求方式" prop="httpMethod">
              <el-select v-model="form.httpMethod" placeholder="请输入请求方式" clearable style="width: 100%"
                         :disabled="form.menuExtFlag === '1'">
                <template v-for="item in httpMethodOptionList">
                  <el-option :value="item.dictValue" :label="item.dictLabel"/>
                </template>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="resStatus">
              <el-radio-group v-model="form.resStatus">
                <template v-for="item in statusOptionList">
                  <el-radio :label="item.dictValue">{{ item.dictLabel }}</el-radio>
                </template>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>


        <!--    菜单 和 目录 相关    -->
        <el-row v-if="form.type === '1' || form.type === '2'">
          <el-col :span="12">
            <el-form-item label="外链菜单" prop="menuExtFlag">
              <el-radio-group v-model="form.menuExtFlag" @input="change($event)" @change="changeExtFlag">
                <el-radio-button label="1">是</el-radio-button>
                <el-radio-button label="2">否</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单缓存" prop="menuCacheFlag">
              <el-radio-group v-model="form.menuCacheFlag" @input="change($event)">
                <el-radio-button label="1">是</el-radio-button>
                <el-radio-button label="2">否</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <!--    菜单 和 目录 相关  并且  不是外链菜单 -->
        <el-row v-if="(form.type === '1' || form.type === '2') && form.menuExtFlag ==='2'">
          <el-col :span="12">
            <el-form-item label="菜单组件名称" prop="componentName">
              <el-input v-model="form.componentName" placeholder="请输入菜单组件名称" @input="change($event)"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单组件地址" prop="componentPath">
              <el-input v-model="form.componentPath" placeholder="请输入菜单组件地址" @input="change($event)"/>
            </el-form-item>
          </el-col>
        </el-row>


        <!--    菜单 和 目录相关    -->
        <el-row v-if="form.type === '1' || form.type === '2'">
          <el-col :span="12">
            <el-form-item label="菜单和目录可见" prop="menuHiddenFlag">
              <el-radio-group v-model="form.menuHiddenFlag" @input="change($event)">
                <el-radio-button label="1">是</el-radio-button>
                <el-radio-button label="2">否</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单图标" prop="menuIcon">
              <el-popover
                placement="bottom-start"
                width="350"
                trigger="click"
                @show="$refs['iconSelect'].reset()"
              >
                <IconSelect ref="iconSelect" @selected="iconSelected"/>
                <el-input slot="reference" v-model="form.menuIcon" placeholder="点击选择图标" readonly>
                  <svg-icon v-if="form.menuIcon" slot="prefix" :icon-class="form.menuIcon" class="el-input__icon"/>
                  <i v-else slot="prefix" class="el-icon-search el-input__icon"/>
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="路由地址" prop="routePath" v-if="form.type === '1' || form.type === '2'">
          <el-input v-model="form.routePath" placeholder="请输入路由地址" @input="change($event)"/>
        </el-form-item>
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
import {addResource, deleteResourceById, getMenuList, updateResource} from '@/api/admin/resource/resource'
import {ConfirmCustom, MessageError, MessageSuccess} from '_l/message'
import {mapActions} from "vuex";
import TablePage from "_c/CommonForm/table-page";
import VueTreeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import IconSelect from "@/components/IconSelect";

export default {
  name: 'resource-manage',
  components: {IconSelect, TablePage, VueTreeselect},
  data() {
    return {
      tableUrl: '/system/resource/getResourceTreeList',
      columns: [
        {label: '标题', prop: 'title', align: 'left', width: '400'},
        {label: '类型', slot: 'type'},
        {label: '权限标识', prop: 'permission'},
        {label: '状态', slot: 'resStatus'},
        {label: '排序', prop: 'resSort'},
        {label: '创建时间', prop: 'createDate'},
        {label: '操作', slot: 'action'}
      ],
      dialogShow: false,
      dialogTitle: '添加模块',
      form: {},
      rules: {},
      statusOptionList: [],
      httpMethodOptionList: [],
      resourceTypeOptionList: [],
      expansion: false,
      menuList: [],
      normalizer(node) {
        return {
          id: node.id,
          label: node.title,
          children: node.children
        }
      }
    }
  },
  watch: {
    'form.type': function (newVal, oldVal) {
      switch (newVal) {
        case '1':
        case '2':
          this.$set(this.form, 'menuExtFlag', '2')
          this.$set(this.form, 'menuCacheFlag', '2')
          this.$set(this.form, 'menuHiddenFlag', '1')
          break;
        case '3':
          this.$set(this.form, 'menuExtFlag', null)
          this.$set(this.form, 'menuCacheFlag', null)
          this.$set(this.form, 'menuHiddenFlag', null)
          this.$set(this.form, 'routePath', null)
          this.$set(this.form, 'componentName', null)
          this.$set(this.form, 'componentPath', null)
          this.$set(this.form, 'menuIcon', null)
          break;
      }
    },
    'form.menuExtFlag': function (newVal, oldVal) {
      if (newVal === '1') {
        this.$set(this.form, 'permission', null)
        this.$set(this.form, 'resPath', null)
        this.$set(this.form, 'httpMethod', null)
      }
    }
  },
  methods: {
    ...mapActions(['getDictListByType']),
    change(e) {
      this.$forceUpdate()
    },
    changeExtFlag(val) {
      this.$set(this.form, 'menuExtFlag', val)
    },
    /**
     * 刷新
     */
    refresh() {
      this.$refs.tablePage.refresh()
    },
    addResource() {
      this.dialogTitle = '添加模块'
      this.form = {
        parentId: 0,
        parentIds: 0,
        // 目录
        type: '1',
        // 状态
        resStatus: '0',
        // 排序
        resSort: '500',
        // 菜单和目录是可见的
        menuHiddenFlag: '1',
        // 不是 外链菜单
        menuExtFlag: '2',
        // 不是 菜单缓存
        menuCacheFlag: '2'
      }
      this.dialogShow = true
    },
    /**
     * 修改弹出框
     * @param row
     */
    update(row) {
      this.form = {...row}
      this.dialogTitle = '修改模块'
      this.dialogShow = true
    },
    /**
     * 行内删除
     * @param row
     */
    remove(row) {
      ConfirmCustom({type: 'warning'}).then(() => {
        deleteResourceById(row.id).then(res => {
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
      this.$refs[name].validate((valid) => {
        if (valid) {
          if (this.dialogTitle === '添加模块') {
            addResource(this.form).then(res => {
              if (res.data) {
                MessageSuccess('add')
              } else {
                MessageError('add')
              }
              this.cancelDialogAndRefresh()
            })
          } else {
            updateResource(this.form).then(res => {
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
    toggleRowExpansion(isExpansion) {
      this.expansion = !this.expansion
      this.toggleRowExpansion_forAll(this.$refs.tablePage.$refs.tableMain.data, isExpansion);
    },
    toggleRowExpansion_forAll(data, isExpansion) {
      data.forEach(item => {
        this.$refs.tablePage.$refs.tableMain.toggleRowExpansion(item, isExpansion);
        if (item.children !== undefined && item.children !== null) {
          this.toggleRowExpansion_forAll(item.children, isExpansion);
        }
      })
    },
    // 选中图标
    iconSelected(name) {
      this.form.menuIcon = name
      this.$forceUpdate()
    },
    treeSelect(node, instanceId) {
      if (node.parentIds !== 0) {
        this.form.parentIds = node.parentIds + ',' + node.id
      } else {
        this.form.parentIds = node.id
      }
    }
  },
  mounted() {
    this.getDictListByType('res_status').then(res => {
      this.statusOptionList = res
    })
    this.getDictListByType('resource_type').then(res => {
      this.resourceTypeOptionList = res
    })
    this.getDictListByType('http_method').then(res => {
      this.httpMethodOptionList = res
    })
    getMenuList().then(res => {
      this.menuList.unshift({id: 0, title: '顶级类目', parentIds: 0, children: res.data})
    })
  }
}
</script>

<style scoped lang="scss">

</style>
