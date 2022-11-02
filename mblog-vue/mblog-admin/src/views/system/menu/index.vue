<template>
  <div>
    <div class="app-container">
      <!-- 查询和其他操作 -->
      <el-row>
        <el-col :span="1.5">
          <el-button size="small" type="primary" icon="el-icon-plus" @click="handleCreate(0)">添加 </el-button>
        </el-col>
        <el-col :span="1.5" :offset="21">
          <el-tooltip class="item" effect="dark" content="刷新" placement="top">
            <el-button size="mini" circle icon="el-icon-refresh" @click="refresh()" />
          </el-tooltip>
        </el-col>
      </el-row>
      <el-table :data="menuList" style="width: 100%">
        <el-table-column type="expand">
          <template slot-scope="scope">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-table :data="scope.row.children" :show-header="showHeader" style="width: 100%">

                <el-table-column label width="60" align="center">
                  <template slot-scope="scope_child">
                    <span>{{ scope_child.row.id }}</span>
                  </template>
                </el-table-column>

                <el-table-column label width="150" align="center">
                  <template slot-scope="scope_child">
                    <span>{{ scope_child.row.title }}</span>
                  </template>
                </el-table-column>

                <el-table-column label width="100" align="center">
                  <template slot-scope="scope_child">
                    <el-tag :type="menuLevelType[scope_child.row.level]">
                      {{ menuLevelOptions[scope_child.row.level] }}
                    </el-tag>
                  </template>
                </el-table-column>

                <el-table-column label width="100" align="center">
                  <template slot-scope="scope_child">
                    <span v-if="scope_child.row.icon != null">
                      <i v-if="scope_child.row.icon.indexOf('el-') > -1" :class="scope_child.row.icon"></i>
                      <svg-icon :icon-class="scope_child.row.icon" />
                    </span>
                  </template>
                </el-table-column>

                <el-table-column label width="200" align="center">
                  <template slot-scope="scope_child">
                    <span>{{ scope_child.row.url }}</span>
                  </template>
                </el-table-column>

                <el-table-column width="100" align="center">
                  <template slot-scope="scope_child">
                    <el-tag :type="hiddenTypes[scope_child.row.hidden]">
                      {{ hiddenOptions[scope_child.row.hidden] }}
                    </el-tag>
                  </template>
                </el-table-column>

                <el-table-column width="100" align="center">
                  <template slot-scope="scope_child">
                    <el-tag type="warning">{{ scope_child.row.sortNo }}</el-tag>
                  </template>
                </el-table-column>

                <el-table-column align="center" min-width="230">
                  <template slot-scope="scope_child">
                    <el-button type="primary" size="mini" @click="handleUpdate(scope_child.row)">编辑</el-button>
                    <el-button size="mini" type="danger" @click="handleDelete(scope_child)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-form>
          </template>
        </el-table-column>

        <el-table-column label="菜单ID" width="60" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.id }}</span>
          </template>
        </el-table-column>

        <el-table-column label="菜单名称" width="150" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.title }}</span>
          </template>
        </el-table-column>

        <el-table-column label="菜单级别" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="menuLevelType[scope.row.level]">
              {{ menuLevelOptions[scope.row.level] }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="图标" width="100" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.icon != null">
              <i v-if="scope.row.icon.indexOf('el-') > -1" :class="scope.row.icon"></i>
              <svg-icon :icon-class="scope.row.icon" />
            </span>
          </template>
        </el-table-column>

        <el-table-column label="路由" width="200" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.url }}</span>
          </template>
        </el-table-column>

        <el-table-column label="是否隐藏" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="hiddenTypes[scope.row.hidden]">
              {{ hiddenOptions[scope.row.hidden] }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="排序" width="100" align="center">
          <template slot-scope="scope">
            <el-tag type="warning">{{ scope.row.sortNo }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" min-width="270" fixed="right">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button type="warning" size="mini" v-if="scope.row.level === 0" @click="handleCreate(scope.row.id)">添加下级
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>


    <!-- 添加或修改系统菜单对话框 -->
    <el-dialog center :title="title" :visible.sync="open">
      <el-form :rules="rules" ref="dataForm" :model="form">
        <el-form-item prop="id" label="菜单ID" label-width="120px">
          <el-input v-if="isEditForm" disabled v-model="form.id" autocomplete="off"></el-input>
          <el-input v-else v-model="form.id" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="title" label="菜单名称" label-width="120px">
          <el-input v-model="form.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="title" label="路由名称" label-width="120px">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="url" label="路由地址" label-width="120px">
          <el-input v-model="form.url" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="component" label="菜单组件" label-width="120px">
          <el-input v-if="form.parentId" v-model="form.component" autocomplete="off"></el-input>
          <el-input v-else disabled v-model="form.component" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="菜单图标" label-width="120px" prop="icon">
          <el-input v-model="form.icon" placeholder="请输入前图标名称">
            <el-button slot="append" icon="el-icon-setting" @click="openIconsDialog('prefix-icon')">
              选择
            </el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="菜单级别" label-width="120px" prop="level">
          <el-select v-model="form.level" placeholder="请选择">
            <el-option v-for="(item, index) in menuLevelOptions" :key="index" :label="item" :value="index">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sortNo" label-width="120px">
          <el-input v-model="form.sortNo" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="hidden" label="是否隐藏" label-width="120px">
          <el-radio-group v-model="form.hidden" size="small">
            <el-radio v-for="(item, index) in hiddenOptions" :key="index" :label="index" border>{{ item }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" label-width="120px">
          <el-input v-model="form.remarks" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
    <icons-dialog :visible.sync="iconsVisible" :current="form.icon" @select="setIcon" />
  </div>
</template>

<script>
import { listMenu, delMenu, addMenu, updateMenu } from "@/api/system/menu";
import IconsDialog from "../../../components/IconsDialog";

export default {
  name: "Menu",
  components: {
    IconsDialog
  },
  data() {
    return {
      // 表单选项
      hiddenTypes: ['warning', 'success'],
      hiddenOptions: ['否', '是'],
      menuLevelType: ['success', 'danger', 'warning'],
      menuLevelOptions: ['一级菜单', '二级菜单'],
      isEditForm: 0,
      // 遮罩层
      loading: [],
      // 是否显示表头
      showHeader: false,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 权限资源 表格数据
      menuList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 图标显示层
      iconsVisible: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        "id": [
          { required: true, message: '请输入菜单ID', trigger: 'change' },
          { pattern: /^[0-9]\d*$/, message: 'ID字段只能为自然数' },
        ],
        "title": [
          { required: true, message: '请输入菜单名称', trigger: 'change' },
          { min: 1, max: 6, message: '长度在1到6个字符' },
        ],
        "url": [
          { required: true, message: '请输入url', trigger: 'change' }
        ],
        "component": [
          { required: true, message: '请输入路由地址', trigger: 'change' }
        ],
        "icon": [
          { required: true, message: '请选择图标', trigger: 'blur' }
        ],
        "level": [
          { required: true, message: '请选择菜单级别', trigger: 'change' }
        ],
        "sortNo": [
          { required: true, message: '请输入排序', trigger: 'change' },
          { pattern: /^[0-9]\d*$/, message: '排序字段只能为自然数' },
        ]
      }
    };
  },
  created() {
    this.openLoading()
    this.getList();
  },
  methods: {
    refresh() {
      this.openLoading()
      this.getList();
    },
    /** 查询权限资源 列表 */
    getList() {
      listMenu(this.queryParams).then(response => {
        this.menuList = response.data;
        this.loading.close();
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        parentId: null,
        title: null,
        url: null,
        component: null,
        level: null,
        sortNo: null,
        icon: null,
        type: null,
        redirect: null,
        name: null,
        hidden: null,
        isCache: null,
        remark: null,
        createTime: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加权限资源 ";
    },
    /** 添加菜单 */
    handleCreate(id) {
      this.form = this.getFormObject(id)
      let title = '添加下级'
      if (!id) {
        this.form.component = 'Layout'
        title = '添加菜单'
      }
      this.beforeShow(0, title)
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    /** 获取表单对象 */
    getFormObject(id) {
      return {
        id: '',
        parentId: id,
        url: '',
        component: null,
        type: 'M',
        title: '',
        level: id ? 1 : 0,
        sortNo: 0,
        hidden: 0,
        remarks: ''
      }
    },
    /** 判断是否可以修改并设置标题 */
    beforeShow(isEditForm, title) {
      this.isEditForm = isEditForm
      this.title = title
      this.open = true
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.form = row
      this.beforeShow(1, '修改菜单')
    },
    /** 选择图标 */
    setIcon(val) {
      this.form.icon = val
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["dataForm"].validate(valid => {
        if (valid) {
          if (this.isEditForm) {
            updateMenu(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMenu(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      console.log(row)
      const id = row.row.id;
      this.$modal.confirm('是否确认删除权限资源 编号为"' + id + '"的数据项？').then(function () {
        return delMenu(id);
      }).then(() => {
        // TODO 删除菜单刷新页面重新获取路由，使得菜单栏也重新刷新
        this.getList()
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 打开ICON图标选择器 */
    openIconsDialog(model) {
      this.iconsVisible = true
      this.currentIconModel = model
    },
    /** 打开加载层 */
    openLoading() {
      this.loading = this.$loading({
        lock: true,
        text: "正在加载中~",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
    },
  }
};
</script>
