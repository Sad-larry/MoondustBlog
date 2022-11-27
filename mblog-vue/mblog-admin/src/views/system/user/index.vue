<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px"
      @submit.native.prevent>
      <el-form-item label="用户名称" prop="username">
        <el-input v-model="queryParams.username" placeholder="请输入用户名称" clearable @clear="resetQuery"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="登录方式" prop="loginType">
        <el-select v-model="queryParams.loginType" filterable clearable reserve-keyword @change='handleQuery'
          placeholder="请选择登录方式">
          <el-option v-for="item in dictLoginTypeList" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">查找</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" fit :data="userList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="avatar" align="center" width="80" label="头像">
        <template slot-scope="scope">
          <img :src="scope.row.avatar" width="60" height="60" />
        </template>
      </el-table-column>
      <el-table-column prop="nickname" width="120" align="center" label="昵称" />
      <el-table-column prop="loginType" align="center" label="登录方式">
        <template slot-scope="scope">
          <span v-for="(item, index) in dictLoginTypeList" :key="index">
            <el-tag v-if="scope.row.loginType === parseInt(item.value)" :type="item.style">
              {{ item.label }}
            </el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="用户角色">
        <template slot-scope="scope">
          <span v-for="(item, index) in roleList" :key="index">
            <el-tag v-if="scope.row.roleId === item.id">
              {{ item.name }}
            </el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="ipAddress" width="125" align="center" label="登录IP" />
      <el-table-column prop="ipSource" width="150" align="center" label="登录地址" />
      <el-table-column prop="createTime" align="center" width="150" label="创建时间">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" width="150" label="最后登录时间">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lastLoginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态">
        <template slot-scope="scope">
          <el-tag>{{ statusOptions[scope.row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <el-dialog center :title="title" :visible.sync="open">
      <el-form :rules="rules" ref="dataForm" :model="form">
        <el-form-item prop="nickName" label="昵称">
          <el-input disabled v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="status" label="状态">
          <div>
            <el-radio v-for="(item, index) in statusOptions" v-model="form.status" :label="index" border :key="index">
              {{ item }}</el-radio>
          </div>
        </el-form-item>
        <el-form-item prop="roleId" label="角色">
          <div>
            <el-radio v-for="(item, index) in roleList" v-model="form.roleId" :label="item.id" border :key="index">{{
                item.name
            }}
            </el-radio>
          </div>
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
import { listUser, getUser, delUser, addUser, updateUser } from "@/api/system/user";
import { listRole } from "@/api/system/role";
import { getDataByDictType } from "@/api/system/dictData";

export default {
  name: "User",
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
      statusOptions: ['禁用', '正常'],
      dictLoginTypeList: [],
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 【请填写功能名称】表格数据
      userList: [],
      roleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        username: null,
        loginType: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在1到20个字符' },
        ],
        nickname: [
          { required: true, message: '请输入昵称', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在1到20个字符' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' },
        ],
        roleId: [
          { required: true, message: '请选择角色', trigger: 'change' },
        ]
      }
    };
  },
  created() {
    this.getDictList();
    this.getRoleList();
    this.getList();
  },
  methods: {
    /** TODO 查询登录方式字典数据 */
    getDictList() {
      let params = ['sys_login_method'];
      getDataByDictType(params).then(response => {
        let dictMap = response.data
        this.dictLoginTypeList = dictMap.sys_login_method.list
        this.loginTypeDefaultValue = dictMap.sys_login_method.defaultValue
      }).catch(err => {
        console.error(err)
      })
    },
    /** TODO 查询角色列表 */
    getRoleList() {
      listRole().then(response => {
        this.roleList = response.data.records;
      })
    },
    /** 查询【请填写功能名称】列表 */
    getList() {
      this.loading = true;
      listUser(this.queryParams).then(response => {
        this.userList = response.data.records;
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
        userName: null,
        nickName: null,
        password: null,
        mobile: null,
        email: null,
        avatar: null,
        intro: null,
        birthday: null,
        status: 0,
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加【请填写功能名称】";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getUser(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改【请填写功能名称】";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["dataForm"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateUser(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUser(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除【请填写功能名称】编号为"' + ids + '"的数据项？').then(function () {
        return delUser(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
  }
};
</script>
