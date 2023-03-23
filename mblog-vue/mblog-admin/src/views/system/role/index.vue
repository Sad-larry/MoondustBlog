<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
      @submit.native.prevent
    >
      <el-form-item label="角色名称">
        <el-input
          style="width: 200px"
          size="small"
          v-model="queryParams.name"
          placeholder="请输入角色名称"
          clearable
          @clear="resetQuery"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查找</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="roleList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" />
      <el-table-column align="center" prop="code" label="编码" width="180" />
      <el-table-column align="center" prop="name" label="名称" />
      <el-table-column align="center" prop="createTime" label="创建时间" />
      <el-table-column align="center" prop="remark" label="备注" />
      <el-table-column align="center" label="操作" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改角色对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="dataForm" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入角色编码"/>
        </el-form-item>
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色描述" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入角色描述" />
        </el-form-item>
        <el-form-item label="角色权限">
          <el-tree
            :data="rolePerms"
            show-checkbox
            node-key="id"
            ref="permsTree"
            accordion
            :props="defaultProps"
            :default-checked-keys="rolePermsIds"
            @check="handleCheckChange"
            empty-text="权限不能修改或没有数据"
          ></el-tree>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listRole,
  getRole,
  delRole,
  addRole,
  updateRole,
  getRolePerms,
  getAllPerms,
  updateRolePerms,
} from "@/api/system/role";

export default {
  name: "Role",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      defaultProps: {
        children: "children",
        label: "title",
      },
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 角色表格数据
      roleList: [],
      rolePerms: [],
      rolePermsIds: [],
      // 权限已发生改变，提交数据时，也同时改变权限
      roleHasChanged: false,
      roleChangedIds: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        code: [
          { required: true, message: "请输入角色编码", trigger: "change" },
        ],
        name: [
          { required: true, message: "请输入角色名称", trigger: "change" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handleCheckChange(data, checkData) {
      this.roleHasChanged = true;
      // 本来还想加以判断，判断是否添加还是从待修改的权限中删除
      // 太麻烦了，不如这个来的简单
      this.roleChangedIds = [
        ...this.$refs.permsTree.getCheckedKeys(),
        ...this.$refs.permsTree.getHalfCheckedKeys(),
      ];
    },
    /** 查询角色列表 */
    getList() {
      this.loading = true;
      listRole(this.queryParams).then((response) => {
        this.roleList = response.data.records;
        this.total = response.data.total;
        this.loading = false;
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
        code: null,
        name: null,
        remark: null,
        createTime: null,
        updateTime: null,
      };
      // 不管是添加还是修改，都得初始化
      this.roleHasChanged = false;
      this.rolePerms = [];
      this.rolePermsIds = [];
      this.resetForm("dataForm");
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加角色";
      // 获取所有权限
      getAllPerms().then((res) => {
        this.rolePerms = res.data;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getRole(id).then((res) => {
        if (res.code == 200) {
          this.form = res.data;
          this.title = "修改角色";
          // 获取权限列表
          getRolePerms(res.data.id).then((res) => {
            if (res.code == 200) {
              this.rolePerms = res.allPerms ? res.allPerms : [];
              this.rolePermsIds = res.rolePermsIds ? res.rolePermsIds : [];
              this.open = true;
            }
          });
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["dataForm"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateRole(this.form).then((res) => {
              this.$modal.msgSuccess("角色信息修改成功");
              // 如果角色权限变动，则进行更新
              if (this.roleHasChanged) {
                this.changedRolePerms(this.form.id);
              }
              this.open = false;
              this.getList();
            });
          } else {
            addRole(this.form).then((res) => {
              this.$modal.msgSuccess("新增成功");
              this.changedRolePerms(res.data);
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 修改或添加用户权限 */
    changedRolePerms(id) {
      // 更改角色权限
      updateRolePerms(id, this.roleChangedIds)
        .then((res) => {
          this.$modal.msgSuccess("角色权限分配成功");
        })
        .catch((err) => {
          this.$modal.msgError("角色权限分配失败");
        });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除角色编号为"' + ids + '"的数据项？')
        .then(function () {
          return delRole(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
  },
};
</script>
