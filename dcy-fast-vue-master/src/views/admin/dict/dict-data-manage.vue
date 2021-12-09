<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form ref="queryParams" :model="queryParams" inline label-width="80px" label-position="left">
      <el-form-item label="字典标签">
        <el-input v-model="queryParams.dictLabel" clearable placeholder="请输入字典标签"/>
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
        <el-button icon="el-icon-plus" type="primary" @click="addDictData()">添加</el-button>
        <el-button icon="el-icon-delete" type="primary" @click="removeBatch()" :disabled="delBtnDisabled">删除</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <table-page
      ref="tablePage"
      url="system/dict-data/page"
      :columns="columns"
      :query-params="queryParams"
      @table-select-val="selectVal">
      <template slot-scope="{ row }" slot="dictStatus">
        <el-tag v-for="item in statusOptionList" v-if="row.dictStatus === item.dictValue" :type="item.listClass">
          {{item.dictLabel}}
        </el-tag>
      </template>
      <template slot-scope="{ row }" slot="action">
        <el-button @click="update(row)" type="text">修改</el-button>
        <el-button @click="remove(row)" type="text">删除</el-button>
      </template>
    </table-page>

    <!-- 添加表单 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogShow"
      :close-on-click-modal="false"
      append-to-body
      @close="handleReset('form')">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="字典标签" prop="dictLabel">
          <el-input v-model="form.dictLabel" placeholder="请输入字典标签" @input="change($event)"/>
        </el-form-item>
        <el-form-item label="字典键值" prop="dictValue">
          <el-input v-model="form.dictValue" placeholder="请输入字典键值" @input="change($event)"/>
        </el-form-item>
        <el-form-item label="字典类型" prop="dictType">
          <el-input v-model="form.dictType" disabled placeholder="请输入字典类型" @input="change($event)"/>
        </el-form-item>
        <el-form-item label="字典排序" prop="dictSort">
          <el-input v-model="form.dictSort" placeholder="请输入字典排序" @input="change($event)"/>
        </el-form-item>
        <el-form-item label="样式属性" prop="cssClass">
          <el-input v-model="form.cssClass" placeholder="请输入样式属性" @input="change($event)"/>
        </el-form-item>
        <el-form-item label="表格回显样式" prop="listClass">
          <el-input v-model="form.listClass" placeholder="请输入表格回显样式" @input="change($event)"/>
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
  </div>
</template>

<script>
  import TablePage from "_c/CommonForm/table-page";
  import {addDictData, deleteBatchDictDataById, deleteDictDataById, updateDictData} from "_a/admin/dict/dict-data";
  import {MessageError, MessageSuccess, ConfirmCustom} from '_l/message'
  import {mapActions} from "vuex";

  export default {
    name: 'dict-data-manage',
    components: {TablePage},
    data() {
      return {
        columns: [
          {label: '字典标签', prop: 'dictLabel'},
          {label: '字典键值', prop: 'dictValue'},
          {label: '字典类型', prop: 'dictType'},
          {label: '字典排序', prop: 'dictSort'},
          {label: '样式属性', prop: 'cssClass'},
          {label: '表格回显样式', prop: 'listClass'},
          {label: '状态', slot: 'dictStatus'},
          {label: '操作', slot: 'action'}
        ],
        queryParams: {},
        dialogShow: false,
        delBtnDisabled: true,
        dialogTitle: '添加字典数据',
        form: {},
        rules: {
          dictLabel: [this.$ruler('字典标签')],
          dictValue: [this.$ruler('字典键值')],
          dictType: [this.$ruler('字典类型')],
          dictSort: [this.$ruler('字典排序')],
          dictStatus: [this.$ruler('状态')],
        },
        statusOptionList: [],
      }
    },
    props: {
      dictType: {
        type: String,
        required: true
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
        this.queryParams = {
          dictType: this.dictType
        }
        this.$nextTick(function () {
          this.refresh()
        })
      },
      /**
       * 添加弹出框
       */
      addDictData() {
        this.form = {
          dictType: this.dictType
        };
        this.dialogTitle = '添加字典数据';
        this.dialogShow = true;
      },
      /**
       * 修改弹出框
       * @param row
       */
      update(row) {
        this.form = {...row};
        this.dialogTitle = '修改字典数据';
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
          deleteBatchDictDataById(this.ids).then(res => {
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
          deleteDictDataById(row.id).then(res => {
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
            if (this.dialogTitle === '添加字典数据') {
              addDictData(this.form).then(res => {
                if (res.data) {
                  MessageSuccess('add')
                } else {
                  MessageError('add')
                }
                this.cancelDialogAndRefresh()
              })
            } else {
              updateDictData(this.form).then(res => {
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
    created() {
      // 拼接参数
      this.queryParams.dictType = this.dictType
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
