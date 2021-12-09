<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form ref="queryParams" :model="queryParams" inline label-width="80px" label-position="left">
      <el-form-item label="字典名称">
        <el-input v-model="queryParams.dictName" clearable placeholder="请输入字典名称"/>
      </el-form-item>
      <el-form-item label="字典类型">
        <el-input v-model="queryParams.dictType" clearable placeholder="请输入字典类型"/>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryParams.dictStatus" placeholder="请选择状态" style="width: 100%">
          <template v-for="item in statusOptionList">
            <el-option :value="item.dictValue" :label="item.dictLabel"/>
          </template>
        </el-select>
      </el-form-item>
      <el-form-item label-width="0px">
        <el-button icon="el-icon-search" type="primary" @click="refresh()">查询</el-button>
        <el-button icon="el-icon-refresh" type="primary" @click="reset()">重置</el-button>
        <el-button icon="el-icon-plus" type="primary" v-permission="['dict:save']" @click="addDictType()">添加</el-button>
        <el-button icon="el-icon-delete" type="primary" v-permission="['dict:delete']" @click="removeBatch()" :disabled="delBtnDisabled">删除</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <table-page
      ref="tablePage"
      url="system/dict-type/page"
      :columns="columns"
      :query-params="queryParams"
      @table-select-val="selectVal">
      <template slot-scope="{ row }" slot="dictStatus">
        <el-tag v-for="item in statusOptionList" v-if="row.dictStatus === item.dictValue" :type="item.listClass">
          {{item.dictLabel}}
        </el-tag>
      </template>
      <template slot-scope="{ row }" slot="action">
        <el-button @click="update(row)" v-permission="['dict:update']" type="text">修改</el-button>
        <el-button @click="dictDataList(row)" type="text">列表</el-button>
        <el-button @click="remove(row)" v-permission="['dict:delete']" type="text">删除</el-button>
      </template>
    </table-page>

    <!-- 添加表单 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogShow"
      :close-on-click-modal="false"
      @close="handleReset('form')">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="form.dictName" placeholder="请输入字典名称" @input="change($event)"/>
        </el-form-item>
        <el-form-item label="字典类型" prop="dictType">
          <el-input v-model="form.dictType" placeholder="请输入字典类型" @input="change($event)"/>
        </el-form-item>
        <el-form-item label="状态" prop="dictStatus">
          <el-select v-model="form.dictStatus" placeholder="请选择状态" style="width: 100%">
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

    <!-- 字典项表格-->
    <el-dialog
      title="字典项管理"
      width="80%"
      :visible.sync="dialogDataShow"
      :close-on-click-modal="false">
      <dict-data-manage v-if="dialogDataShow" :dict-type="dictType"></dict-data-manage>
    </el-dialog>
  </div>
</template>

<script>
  import TablePage from "_c/CommonForm/table-page";
  import {addDictType, deleteBatchDictTypeById, deleteDictTypeById, updateDictType} from "_a/admin/dict/dict-type";
  import {MessageError, MessageSuccess, ConfirmCustom} from '_l/message'
  import {mapActions} from "vuex";
  import DictDataManage from "@/views/admin/dict/dict-data-manage";

  export default {
    name: 'dict-type-manage',
    components: {DictDataManage, TablePage},
    data() {
      return {
        columns: [
          {label: '字典名称', prop: 'dictName'},
          {label: '字典类型', prop: 'dictType'},
          {label: '状态', slot: 'dictStatus'},
          {label: '操作', slot: 'action'}
        ],
        queryParams: {},
        dialogShow: false,
        dialogDataShow: false,
        delBtnDisabled: true,
        dialogTitle: '添加字典类型',
        form: {},
        rules: {
          dictName: [this.$ruler('字典名称')],
          dictType: [this.$ruler('字典类型')],
          dictStatus: [this.$ruler('状态')]
        },
        statusOptionList: [],
        dictType:null
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
      addDictType() {
        this.form = {};
        this.dialogTitle = '添加字典类型';
        this.dialogShow = true;
      },
      /**
       * 修改弹出框
       * @param row
       */
      update(row) {
        this.form = {...row};
        this.dialogTitle = '修改字典类型';
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
          deleteBatchDictTypeById(this.ids).then(res => {
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
          deleteDictTypeById(row.id).then(res => {
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
            if (this.dialogTitle === '添加字典类型') {
              addDictType(this.form).then(res => {
                if (res.data) {
                  MessageSuccess('add')
                } else {
                  MessageError('add')
                }
                this.cancelDialogAndRefresh()
              })
            } else {
              updateDictType(this.form).then(res => {
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
      dictDataList(row){
        console.log(row);
        this.dictType = row.dictType
        this.$nextTick(function () {
          this.dialogDataShow = true;
        })
      }
    },
    mounted() {
      this.getDictListByType('dict_status').then(res => {
        this.statusOptionList = res
      })
    }
  }
</script>

<style scoped lang="scss">

</style>
