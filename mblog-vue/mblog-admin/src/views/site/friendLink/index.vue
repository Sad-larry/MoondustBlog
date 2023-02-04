<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px"
      @submit.native.prevent>
      <el-form-item label="友链名称">
        <el-input style="width: 200px" size="small" v-model="queryParams.name" placeholder="请输入友链名"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="友链状态">
        <el-select @change="handleQuery" size="small" v-model="queryParams.status" clearable placeholder="友链状态">
          <el-option v-for="(item, index) in statusOptions" :key="index" :label="item" :value="index">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
          @click="handleDelete">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="linkList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" />
      <el-table-column label="网站图标" width="80" align="center">
        <template slot-scope="scope">
          <el-image v-if="scope.row.avatar" :src="scope.row.avatar" style="width: 50px;height:50px;">
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline" style="margin-top: 21px;"></i>
            </div>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="name" align="center" label="网站名称" width="180" />
      <el-table-column prop="info" align="center" width="180" label="网站描述" />
      <el-table-column align="center" width="180" label="网站地址">
        <template slot-scope="scope">
          <el-link :underline="false" @click="onClick(scope.row.url)">{{ scope.row.url }}</el-link>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="status" label="状态">
        <template slot-scope="scope">
          <span>
            <el-tag :type="statusTypes[scope.row.status]">
              {{ statusOptions[scope.row.status] }}
            </el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="sort" sortable label="排序">
        <template slot-scope="scope">
          <span>
            <el-tag type="warning">{{ scope.row.sort }}</el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="createTime" width="200" label="创建时间">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status === 0" size="mini" type="text" icon="el-icon-finished"
            @click="handlePass(scope.row)">审核通过
          </el-button>
          <el-button v-else-if="scope.row.status === 1" size="mini" type="text" icon="el-icon-top"
            @click="handleTop(scope.row)">置顶
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改友情链接对话框 -->
    <el-dialog center :title="title" :visible.sync="open" append-to-body>
      <el-form :model="form" :rules="rules" ref="dataForm">
        <el-form-item label="网站头像" prop="avatar">
          <el-input v-model="form.avatar" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="网站名称" prop="name">
          <el-input v-model="form.name" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="网站简介" prop="info">
          <el-input v-model="form.info" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="网站地址" prop="url">
          <el-input v-model="form.url" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="站长邮箱" prop="email">
          <el-input v-model="form.email" auto-complete="off"></el-input>
        </el-form-item>
        <!-- <el-form-item label="网站状态" prop="status">
                  <el-select v-model="form.status" size="small" placeholder="请选择">
                      <el-option v-for="(item, index) in statusOptions" :key="index" :label="item" :value="index">
                      </el-option>
                  </el-select>
              </el-form-item> -->
        <el-form-item label="排序" prop="sort">
          <el-input v-model="form.sort" auto-complete="off"></el-input>
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
import { listFriendlink, getFriendlink, delFriendlink, addFriendlink, updateFriendlink, topFriendlink, passFriendlink } from "@/api/system/friendlink";

export default {
  name: "Link",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 友情链接表格数据
      linkList: [],
      statusTypes: ["danger", 'success'],
      statusOptions: ["审核中", "上架"],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        avatar: [
          { required: true, message: '网站头像不能为空', trigger: 'blur' },
        ],
        name: [
          { required: true, message: '网站名称不能为空', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在1到20个字符' },
        ],
        url: [
          { required: true, message: '网站地址不能为空', trigger: 'blur' },
          { pattern: /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/, message: '请输入有效的网站地址' },
        ],
        email: [
          { pattern: /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/, message: '请输入正确的邮箱' },
        ],
        sort: [
          { required: true, message: '排序字段不能为空', trigger: 'blur' },
          { pattern: /^[0-9]\d*$/, message: '排序字段只能为自然数' },
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询友情链接列表 */
    getList() {
      this.loading = true;
      listFriendlink(this.queryParams).then(response => {
        this.linkList = response.records;
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
        name: null,
        url: null,
        avatar: null,
        info: null,
        email: null,
        sort: null,
        status: null,
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
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加友情链接";
    },
    /** 审核通过友链 */
    handlePass(row) {
      const ids = row.id || this.ids;
      passFriendlink(ids).then(() => {
        this.getList();
        this.$modal.msgSuccess("审核通过");
      }).catch(() => { });
    },
    /** 置顶友链 */
    handleTop(row) {
      let friendlink = {
        id: row.id
      }
      topFriendlink(friendlink).then(response => {
        this.$message.success("置顶成功")
        this.getList()
      }).catch(err => {
        console.log(err)
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.form = row;
      this.open = true;
      this.title = "修改友情链接";

    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["dataForm"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateFriendlink(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFriendlink(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除友情链接编号为"' + ids + '"的数据项？').then(function () {
        return delFriendlink(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 打开博客 */
    onClick(url) {
      window.open(url)
    },
  }
};
</script>
