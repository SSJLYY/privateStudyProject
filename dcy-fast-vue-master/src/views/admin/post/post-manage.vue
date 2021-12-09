<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form ref="queryParams" :model="queryParams" inline label-width="60px" label-position="left">
      <el-form-item label="名称">
        <el-input v-model="queryParams.postName" clearable placeholder="请输入岗位名称"/>
      </el-form-item>
      <el-form-item label="状态">
        <el-input v-model="queryParams.postStatus" clearable placeholder="请输入岗位状态"/>
      </el-form-item>
      <el-form-item label-width="0px">
        <el-button icon="el-icon-search" type="primary" @click="refresh()">查询</el-button>
        <el-button icon="el-icon-refresh" type="primary" @click="reset()">重置</el-button>
        <el-button icon="el-icon-plus" type="primary" v-permission="['post:save']" @click="addPost()">添加</el-button>
        <el-button icon="el-icon-delete" type="primary" v-permission="['post:delete']" @click="removeBatch()" :disabled="delBtnDisabled">删除</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <table-page
      ref="tablePage"
      url="system/post/page"
      :columns="columns"
      :query-params="queryParams"
      @table-select-val="selectVal">
      <template slot-scope="{ row }" slot="postStatus">
        <el-tag v-for="item in statusOptionList" v-if="row.postStatus === item.dictValue" :type="item.listClass">
          {{item.dictLabel}}
        </el-tag>
      </template>
      <template slot-scope="{ row }" slot="action">
        <el-button @click="update(row)" v-permission="['post:update']" type="text">修改</el-button>
        <el-button @click="remove(row)" v-permission="['post:delete']" type="text">删除</el-button>
      </template>
    </table-page>

    <!-- 添加表单 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogShow"
      :close-on-click-modal="false"
      @close="handleReset('form')">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="岗位编码" prop="postCode">
          <el-input v-model="form.postCode" placeholder="请输入岗位编码" @input="change($event)"/>
        </el-form-item>
        <el-form-item label="岗位名称" prop="postName">
          <el-input v-model="form.postName" placeholder="请输入岗位名称" @input="change($event)"/>
        </el-form-item>
        <el-form-item label="显示顺序" prop="postSort">
          <el-input-number v-model="form.postSort" :precision="2" :step="0.1" :max="10"></el-input-number>
        </el-form-item>
        <el-form-item label="岗位状态" prop="postStatus">
          <el-select v-model="form.postStatus" placeholder="请输入岗位状态" style="width: 100%">
            <template v-for="item in statusOptionList">
              <el-option :value="item.dictValue" :label="item.dictLabel"/>
            </template>
          </el-select>
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
  import TablePage from "_c/CommonForm/table-page";
  import {addPost, deleteBatchPostById, deletePostById, updatePost} from "_a/admin/post/post";
  import {MessageError, MessageSuccess, ConfirmCustom} from '_l/message'
  import {mapActions} from "vuex";

  export default {
    name: 'post-manage',
    components: {TablePage},
    data() {
      return {
        columns: [
          {label: '岗位编码', prop: 'postCode'},
          {label: '岗位名称', prop: 'postName'},
          {label: '显示顺序', prop: 'postSort'},
          {label: '岗位状态', slot: 'postStatus'},
          {label: '操作', slot: 'action'}
        ],
        queryParams: {},
        dialogShow: false,
        delBtnDisabled: true,
        dialogTitle: '添加岗位',
        form: {},
        statusOptionList:[],
        rules: {
          postCode: [this.$ruler('岗位编码')],
          postName: [this.$ruler('岗位名称')],
          postSort: [this.$ruler('显示顺序')],
          postStatus: [this.$ruler('岗位状态')],
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
      addPost() {
        this.form = {
          postSort:0
        };
        this.dialogTitle = '添加岗位';
        this.dialogShow = true;
      },
      /**
       * 修改弹出框
       * @param row
       */
      update(row) {
        this.form = {...row};
        this.dialogTitle = '修改岗位';
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
          deleteBatchPostById(this.ids).then(res => {
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
          deletePostById(row.postId).then(res => {
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
            if (this.dialogTitle === '添加岗位') {
              addPost(this.form).then(res => {
                if (res.data) {
                  MessageSuccess('add')
                } else {
                  MessageError('add')
                }
                this.cancelDialogAndRefresh()
              })
            } else {
              updatePost(this.form).then(res => {
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
    },
    mounted() {
      this.getDictListByType('post_status').then(res => {
        this.statusOptionList = res
      })
    }
  }
</script>

<style scoped lang="scss">

</style>
