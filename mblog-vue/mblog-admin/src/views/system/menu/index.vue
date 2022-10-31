<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="上级资源ID" prop="parentId">
        <el-input
          v-model="queryParams.parentId"
          placeholder="请输入上级资源ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="资源名称" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入资源名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="路由地址" prop="url">
        <el-input
          v-model="queryParams.url"
          placeholder="请输入路由地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="资源组件" prop="component">
        <el-input
          v-model="queryParams.component"
          placeholder="请输入资源组件"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="资源级别" prop="level">
        <el-input
          v-model="queryParams.level"
          placeholder="请输入资源级别"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="显示顺序" prop="sortNo">
        <el-input
          v-model="queryParams.sortNo"
          placeholder="请输入显示顺序"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="资源图标" prop="icon">
        <el-input
          v-model="queryParams.icon"
          placeholder="请输入资源图标"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="重定向地址" prop="redirect">
        <el-input
          v-model="queryParams.redirect"
          placeholder="请输入重定向地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="跳转地址" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入跳转地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否隐藏(0否,1是)" prop="hidden">
        <el-input
          v-model="queryParams.hidden"
          placeholder="请输入是否隐藏(0否,1是)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否缓存" prop="isCache">
        <el-input
          v-model="queryParams.isCache"
          placeholder="请输入是否缓存"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:menu:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:menu:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:menu:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:menu:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="menuList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="上级资源ID" align="center" prop="parentId" />
      <el-table-column label="资源名称" align="center" prop="title" />
      <el-table-column label="路由地址" align="center" prop="url" />
      <el-table-column label="资源组件" align="center" prop="component" />
      <el-table-column label="资源级别" align="center" prop="level" />
      <el-table-column label="显示顺序" align="center" prop="sortNo" />
      <el-table-column label="资源图标" align="center" prop="icon" />
      <el-table-column label="菜单类型(M菜单,F按钮)" align="center" prop="type" />
      <el-table-column label="重定向地址" align="center" prop="redirect" />
      <el-table-column label="跳转地址" align="center" prop="name" />
      <el-table-column label="是否隐藏(0否,1是)" align="center" prop="hidden" />
      <el-table-column label="是否缓存" align="center" prop="isCache" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:menu:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:menu:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改权限资源 对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="上级资源ID" prop="parentId">
          <el-input v-model="form.parentId" placeholder="请输入上级资源ID" />
        </el-form-item>
        <el-form-item label="资源名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入资源名称" />
        </el-form-item>
        <el-form-item label="路由地址" prop="url">
          <el-input v-model="form.url" placeholder="请输入路由地址" />
        </el-form-item>
        <el-form-item label="资源组件" prop="component">
          <el-input v-model="form.component" placeholder="请输入资源组件" />
        </el-form-item>
        <el-form-item label="资源级别" prop="level">
          <el-input v-model="form.level" placeholder="请输入资源级别" />
        </el-form-item>
        <el-form-item label="显示顺序" prop="sortNo">
          <el-input v-model="form.sortNo" placeholder="请输入显示顺序" />
        </el-form-item>
        <el-form-item label="资源图标" prop="icon">
          <el-input v-model="form.icon" placeholder="请输入资源图标" />
        </el-form-item>
        <el-form-item label="重定向地址" prop="redirect">
          <el-input v-model="form.redirect" placeholder="请输入重定向地址" />
        </el-form-item>
        <el-form-item label="跳转地址" prop="name">
          <el-input v-model="form.name" placeholder="请输入跳转地址" />
        </el-form-item>
        <el-form-item label="是否隐藏(0否,1是)" prop="hidden">
          <el-input v-model="form.hidden" placeholder="请输入是否隐藏(0否,1是)" />
        </el-form-item>
        <el-form-item label="是否缓存" prop="isCache">
          <el-input v-model="form.isCache" placeholder="请输入是否缓存" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
import { listMenu, getMenu, delMenu, addMenu, updateMenu } from "@/api/system/menu";

export default {
  name: "Menu",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 权限资源 表格数据
      menuList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
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
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询权限资源 列表 */
    getList() {
      this.loading = true;
      listMenu(this.queryParams).then(response => {
        this.menuList = response.rows;
        this.total = response.total;
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加权限资源 ";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMenu(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改权限资源 ";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除权限资源 编号为"' + ids + '"的数据项？').then(function() {
        return delMenu(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/menu/export', {
        ...this.queryParams
      }, `menu_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
