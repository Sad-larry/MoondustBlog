<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="七牛云公钥" prop="qiNiuAccessKey">
        <el-input
          v-model="queryParams.qiNiuAccessKey"
          placeholder="请输入七牛云公钥"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="七牛云私钥" prop="qiNiuSecretKey">
        <el-input
          v-model="queryParams.qiNiuSecretKey"
          placeholder="请输入七牛云私钥"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="七牛云存储区域" prop="qiNiuArea">
        <el-input
          v-model="queryParams.qiNiuArea"
          placeholder="请输入七牛云存储区域"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="七牛云上传空间" prop="qiNiuBucket">
        <el-input
          v-model="queryParams.qiNiuBucket"
          placeholder="请输入七牛云上传空间"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="七牛云域名前缀+http" prop="qiNiuPictureBaseUrl">
        <el-input
          v-model="queryParams.qiNiuPictureBaseUrl"
          placeholder="请输入七牛云域名前缀+http"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件上传七牛云(0否,1是)" prop="uploadQiNiu">
        <el-input
          v-model="queryParams.uploadQiNiu"
          placeholder="请输入文件上传七牛云(0否,1是)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否开启注册用户邮件激活(0否,1是)" prop="openEmailActivate">
        <el-input
          v-model="queryParams.openEmailActivate"
          placeholder="请输入是否开启注册用户邮件激活(0否,1是)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否开启邮件通知(0否,1是)" prop="startEmailNotification">
        <el-input
          v-model="queryParams.startEmailNotification"
          placeholder="请输入是否开启邮件通知(0否,1是)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否开启仪表盘通知(0否,1是)" prop="openDashboardNotification">
        <el-input
          v-model="queryParams.openDashboardNotification"
          placeholder="请输入是否开启仪表盘通知(0否,1是)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="搜索模式(0SQL搜索,1全文检索)" prop="searchModel">
        <el-input
          v-model="queryParams.searchModel"
          placeholder="请输入搜索模式(0SQL搜索,1全文检索)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="邮箱地址" prop="emailHost">
        <el-input
          v-model="queryParams.emailHost"
          placeholder="请输入邮箱地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="邮箱发件人" prop="emailUsername">
        <el-input
          v-model="queryParams.emailUsername"
          placeholder="请输入邮箱发件人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="邮箱授权码" prop="emailPassword">
        <el-input
          v-model="queryParams.emailPassword"
          placeholder="请输入邮箱授权码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="邮箱发送端口" prop="emailPort">
        <el-input
          v-model="queryParams.emailPort"
          placeholder="请输入邮箱发送端口"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="启用邮箱发送(0否,1是)" prop="openEmail">
        <el-input
          v-model="queryParams.openEmail"
          placeholder="请输入启用邮箱发送(0否,1是)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="本地文件地址" prop="localFileUrl">
        <el-input
          v-model="queryParams.localFileUrl"
          placeholder="请输入本地文件地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件上传方式(1本地,2七牛云)" prop="fileUploadWay">
        <el-input
          v-model="queryParams.fileUploadWay"
          placeholder="请输入文件上传方式(1本地,2七牛云)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="阿里云ak" prop="aliYunAccessKey">
        <el-input
          v-model="queryParams.aliYunAccessKey"
          placeholder="请输入阿里云ak"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="阿里云sk" prop="aliYunSecretKey">
        <el-input
          v-model="queryParams.aliYunSecretKey"
          placeholder="请输入阿里云sk"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="阿里云存储桶名" prop="aliYunBucket">
        <el-input
          v-model="queryParams.aliYunBucket"
          placeholder="请输入阿里云存储桶名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="阿里云Endpoint" prop="aliYunEndpoint">
        <el-input
          v-model="queryParams.aliYunEndpoint"
          placeholder="请输入阿里云Endpoint"
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
          v-hasPermi="['system:config:add']"
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
          v-hasPermi="['system:config:edit']"
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
          v-hasPermi="['system:config:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:config:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="configList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="七牛云公钥" align="center" prop="qiNiuAccessKey" />
      <el-table-column label="七牛云私钥" align="center" prop="qiNiuSecretKey" />
      <el-table-column label="七牛云存储区域" align="center" prop="qiNiuArea" />
      <el-table-column label="七牛云上传空间" align="center" prop="qiNiuBucket" />
      <el-table-column label="七牛云域名前缀+http" align="center" prop="qiNiuPictureBaseUrl" />
      <el-table-column label="文件上传七牛云(0否,1是)" align="center" prop="uploadQiNiu" />
      <el-table-column label="是否开启注册用户邮件激活(0否,1是)" align="center" prop="openEmailActivate" />
      <el-table-column label="是否开启邮件通知(0否,1是)" align="center" prop="startEmailNotification" />
      <el-table-column label="是否开启仪表盘通知(0否,1是)" align="center" prop="openDashboardNotification" />
      <el-table-column label="仪表盘通知【用于首次登录弹框】MD" align="center" prop="dashboardNotificationMd" />
      <el-table-column label="仪表盘通知【用于首次登录弹框】" align="center" prop="dashboardNotification" />
      <el-table-column label="搜索模式(0SQL搜索,1全文检索)" align="center" prop="searchModel" />
      <el-table-column label="邮箱地址" align="center" prop="emailHost" />
      <el-table-column label="邮箱发件人" align="center" prop="emailUsername" />
      <el-table-column label="邮箱授权码" align="center" prop="emailPassword" />
      <el-table-column label="邮箱发送端口" align="center" prop="emailPort" />
      <el-table-column label="启用邮箱发送(0否,1是)" align="center" prop="openEmail" />
      <el-table-column label="本地文件地址" align="center" prop="localFileUrl" />
      <el-table-column label="文件上传方式(1本地,2七牛云)" align="center" prop="fileUploadWay" />
      <el-table-column label="阿里云ak" align="center" prop="aliYunAccessKey" />
      <el-table-column label="阿里云sk" align="center" prop="aliYunSecretKey" />
      <el-table-column label="阿里云存储桶名" align="center" prop="aliYunBucket" />
      <el-table-column label="阿里云Endpoint" align="center" prop="aliYunEndpoint" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:config:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:config:remove']"
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

    <!-- 添加或修改系统配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="七牛云公钥" prop="qiNiuAccessKey">
          <el-input v-model="form.qiNiuAccessKey" placeholder="请输入七牛云公钥" />
        </el-form-item>
        <el-form-item label="七牛云私钥" prop="qiNiuSecretKey">
          <el-input v-model="form.qiNiuSecretKey" placeholder="请输入七牛云私钥" />
        </el-form-item>
        <el-form-item label="七牛云存储区域" prop="qiNiuArea">
          <el-input v-model="form.qiNiuArea" placeholder="请输入七牛云存储区域" />
        </el-form-item>
        <el-form-item label="七牛云上传空间" prop="qiNiuBucket">
          <el-input v-model="form.qiNiuBucket" placeholder="请输入七牛云上传空间" />
        </el-form-item>
        <el-form-item label="七牛云域名前缀+http" prop="qiNiuPictureBaseUrl">
          <el-input v-model="form.qiNiuPictureBaseUrl" placeholder="请输入七牛云域名前缀+http" />
        </el-form-item>
        <el-form-item label="文件上传七牛云(0否,1是)" prop="uploadQiNiu">
          <el-input v-model="form.uploadQiNiu" placeholder="请输入文件上传七牛云(0否,1是)" />
        </el-form-item>
        <el-form-item label="是否开启注册用户邮件激活(0否,1是)" prop="openEmailActivate">
          <el-input v-model="form.openEmailActivate" placeholder="请输入是否开启注册用户邮件激活(0否,1是)" />
        </el-form-item>
        <el-form-item label="是否开启邮件通知(0否,1是)" prop="startEmailNotification">
          <el-input v-model="form.startEmailNotification" placeholder="请输入是否开启邮件通知(0否,1是)" />
        </el-form-item>
        <el-form-item label="是否开启仪表盘通知(0否,1是)" prop="openDashboardNotification">
          <el-input v-model="form.openDashboardNotification" placeholder="请输入是否开启仪表盘通知(0否,1是)" />
        </el-form-item>
        <el-form-item label="仪表盘通知【用于首次登录弹框】MD" prop="dashboardNotificationMd">
          <el-input v-model="form.dashboardNotificationMd" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="仪表盘通知【用于首次登录弹框】" prop="dashboardNotification">
          <el-input v-model="form.dashboardNotification" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="搜索模式(0SQL搜索,1全文检索)" prop="searchModel">
          <el-input v-model="form.searchModel" placeholder="请输入搜索模式(0SQL搜索,1全文检索)" />
        </el-form-item>
        <el-form-item label="邮箱地址" prop="emailHost">
          <el-input v-model="form.emailHost" placeholder="请输入邮箱地址" />
        </el-form-item>
        <el-form-item label="邮箱发件人" prop="emailUsername">
          <el-input v-model="form.emailUsername" placeholder="请输入邮箱发件人" />
        </el-form-item>
        <el-form-item label="邮箱授权码" prop="emailPassword">
          <el-input v-model="form.emailPassword" placeholder="请输入邮箱授权码" />
        </el-form-item>
        <el-form-item label="邮箱发送端口" prop="emailPort">
          <el-input v-model="form.emailPort" placeholder="请输入邮箱发送端口" />
        </el-form-item>
        <el-form-item label="启用邮箱发送(0否,1是)" prop="openEmail">
          <el-input v-model="form.openEmail" placeholder="请输入启用邮箱发送(0否,1是)" />
        </el-form-item>
        <el-form-item label="本地文件地址" prop="localFileUrl">
          <el-input v-model="form.localFileUrl" placeholder="请输入本地文件地址" />
        </el-form-item>
        <el-form-item label="文件上传方式(1本地,2七牛云)" prop="fileUploadWay">
          <el-input v-model="form.fileUploadWay" placeholder="请输入文件上传方式(1本地,2七牛云)" />
        </el-form-item>
        <el-form-item label="阿里云ak" prop="aliYunAccessKey">
          <el-input v-model="form.aliYunAccessKey" placeholder="请输入阿里云ak" />
        </el-form-item>
        <el-form-item label="阿里云sk" prop="aliYunSecretKey">
          <el-input v-model="form.aliYunSecretKey" placeholder="请输入阿里云sk" />
        </el-form-item>
        <el-form-item label="阿里云存储桶名" prop="aliYunBucket">
          <el-input v-model="form.aliYunBucket" placeholder="请输入阿里云存储桶名" />
        </el-form-item>
        <el-form-item label="阿里云Endpoint" prop="aliYunEndpoint">
          <el-input v-model="form.aliYunEndpoint" placeholder="请输入阿里云Endpoint" />
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
import { listConfig, getConfig, delConfig, addConfig, updateConfig } from "@/api/system/config";

export default {
  name: "Config",
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
      // 系统配置表格数据
      configList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        qiNiuAccessKey: null,
        qiNiuSecretKey: null,
        qiNiuArea: null,
        qiNiuBucket: null,
        qiNiuPictureBaseUrl: null,
        uploadQiNiu: null,
        openEmailActivate: null,
        startEmailNotification: null,
        openDashboardNotification: null,
        dashboardNotificationMd: null,
        dashboardNotification: null,
        searchModel: null,
        emailHost: null,
        emailUsername: null,
        emailPassword: null,
        emailPort: null,
        openEmail: null,
        localFileUrl: null,
        fileUploadWay: null,
        aliYunAccessKey: null,
        aliYunSecretKey: null,
        aliYunBucket: null,
        aliYunEndpoint: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        startEmailNotification: [
          { required: true, message: "是否开启邮件通知(0否,1是)不能为空", trigger: "blur" }
        ],
        openDashboardNotification: [
          { required: true, message: "是否开启仪表盘通知(0否,1是)不能为空", trigger: "blur" }
        ],
        searchModel: [
          { required: true, message: "搜索模式(0SQL搜索,1全文检索)不能为空", trigger: "blur" }
        ],
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
    /** 查询系统配置列表 */
    getList() {
      this.loading = true;
      listConfig(this.queryParams).then(response => {
        this.configList = response.rows;
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
        qiNiuAccessKey: null,
        qiNiuSecretKey: null,
        qiNiuArea: null,
        qiNiuBucket: null,
        qiNiuPictureBaseUrl: null,
        uploadQiNiu: null,
        openEmailActivate: null,
        startEmailNotification: null,
        openDashboardNotification: null,
        dashboardNotificationMd: null,
        dashboardNotification: null,
        searchModel: null,
        emailHost: null,
        emailUsername: null,
        emailPassword: null,
        emailPort: null,
        openEmail: null,
        localFileUrl: null,
        fileUploadWay: null,
        aliYunAccessKey: null,
        aliYunSecretKey: null,
        aliYunBucket: null,
        aliYunEndpoint: null,
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
      this.title = "添加系统配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getConfig(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改系统配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addConfig(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除系统配置编号为"' + ids + '"的数据项？').then(function() {
        return delConfig(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/config/export', {
        ...this.queryParams
      }, `config_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
