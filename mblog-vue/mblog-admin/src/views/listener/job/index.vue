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
      <el-form-item label="任务名称" prop="jobName">
        <el-input
          v-model="queryParams.jobName"
          placeholder="请输入任务名称"
          clearable
          @clear="resetQuery"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务组名" prop="jobGroup">
        <el-select @change="handleQuery" v-model="queryParams.jobGroup" placeholder="请选择任务组名" clearable size="small">
          <el-option v-for="dict in jobDictList" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="任务状态" prop="status">
        <el-select @change="handleQuery" v-model="queryParams.status" placeholder="请选择任务状态" clearable size="small">
          <el-option v-for="dict in jobStatusList" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
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
          v-hasPermi="['system:job:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          @click="handleDelete"
          v-hasPermi="['system:job:delete']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="jobList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="任务名称" width="150" align="center" prop="jobName" :show-overflow-tooltip="true" />
      <el-table-column label="任务组名" align="center" prop="jobGroup">
        <template slot-scope="scope">
          <template v-for="(dict, index) in jobDictList">
            <el-tag v-if="dict.value === scope.row.jobGroup" :key="index" :type="dict.style">{{ dict.label }}</el-tag>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="调用目标字符串" align="center" prop="invokeTarget" :show-overflow-tooltip="true" />
      <el-table-column label="cron执行表达式" align="center" prop="cronExpression" />
      <el-table-column label="开启任务" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="handleStatusChange(scope.row)"
            v-hasPermi="['system:job:change']"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" width="160">
        <template slot-scope="scope">{{ parseTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="250">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="warning"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:job:update']"
          >修改</el-button>
          <el-button
            size="mini"
            type="danger"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:job:delete']"
          >删除</el-button>
          <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)">
            <el-button size="mini" type="primary" style="margin-left: 9px">更多</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="handleRun" icon="el-icon-caret-right" v-hasPermi="['system:job:run']">执行一次</el-dropdown-item>
              <el-dropdown-item command="handleView" icon="el-icon-view" v-hasPermi="['system:job:info']">任务详细</el-dropdown-item>
              <el-dropdown-item
                command="handleJobLog"
                icon="el-icon-s-operation"
                v-hasPermi="['system:jobLog:list']"
              >调度日志</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @size-change="getList"
    />

    <!-- Cron表达式生成器 -->
    <el-dialog title="Cron表达式生成器" :visible.sync="openCron" append-to-body destroy-on-close class="scrollbar">
      <crontab @hide="openCron = false" @fill="crontabFill" :expression="expression"></crontab>
    </el-dialog>

    <!-- 添加或修改定时任务对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="dataForm" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务名称" prop="jobName">
              <el-input v-model="form.jobName" placeholder="请输入任务名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务分组" prop="jobGroup">
              <el-select v-model="form.jobGroup" placeholder="请选择">
                <el-option v-for="dict in jobDictList" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item prop="invokeTarget">
              <span slot="label">
                调用方法
                <el-tooltip placement="top">
                  <div slot="content">
                    Bean调用示例：blogQuartz.blogParams('blog')
                    <br />Class类调用示例：com.example.quartz.BlogQuartz.blogParams('blog')
                    <br />参数说明：支持字符串，布尔类型，长整型，双精度浮点型，整型
                  </div>
                  <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
              <el-input v-model="form.invokeTarget" placeholder="请输入调用目标字符串" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="cron表达式" prop="cronExpression">
              <el-input v-model="form.cronExpression" placeholder="请输入cron执行表达式">
                <template slot="append">
                  <el-button type="primary" @click="handleShowCron">
                    生成表达式
                    <i class="el-icon-time el-icon--right"></i>
                  </el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="错误策略" prop="misfirePolicy">
              <el-radio-group v-model="form.misfirePolicy" size="small">
                <el-radio-button v-for="item in jobMisfireList" :key="item.value" :label="item.value">{{ item.label }}</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否并发" prop="concurrent">
              <el-radio-group v-model="form.concurrent" size="small">
                <el-radio-button label="1">允许</el-radio-button>
                <el-radio-button label="0">禁止</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in jobStatusList" :key="dict.value" :label="parseInt(dict.value)">{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注">
              <el-input v-model="form.remark" placeholder="任务备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 任务日志详细 -->
    <el-dialog title="任务详细" :visible.sync="openView" width="700px" append-to-body>
      <el-form ref="form" :model="form" label-width="120px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务编号：">{{ form.jobId }}</el-form-item>
            <el-form-item label="任务名称：">{{ form.jobName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <template v-for="(item, index) in jobDictList">
              <el-form-item v-if="item.value === form.jobGroup" :key="index" label="任务分组：">{{ item.label }}</el-form-item>
            </template>
            <el-form-item label="创建时间：">{{ parseTime(form.createTime) }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="cron表达式：">{{ form.cronExpression }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下次执行时间：">{{ parseTime(form.nextValidTime) }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="调用目标方法：">{{ form.invokeTarget }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务状态：">
              <template v-for="(item, index) in jobStatusList">
                <div v-if="form.status == item.value" :key="index">{{ item.label }}</div>
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否并发：">
              <div v-if="form.concurrent === 1">允许</div>
              <div v-else-if="form.concurrent === 0">禁止</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行策略：">
              <template v-for="(item, index) in jobMisfireList">
                <div v-if="form.misfirePolicy == item.value" :key="index">{{ item.label }}</div>
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注：">{{ form.remark }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listJob,
  getJob,
  delJob,
  addJob,
  updateJob,
  run,
  change,
} from "@/api/system/job";
import { getDataByDictType } from "@/api/system/dictData";
import Crontab from "@/components/Crontab";

export default {
  name: "Job",
  components: {
    Crontab,
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 定时任务调度表格数据
      jobList: [],
      // 任务组名列表
      jobDictList: [],
      jobDictDefaultValue: null,
      // 任务状态列表
      jobStatusList: [],
      jobStatusDefaultValue: null,
      // 任务执行策略
      jobMisfireList: [],
      jobMisfireDefaultValue: null,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openCron: false,
      openView: false,
      // 传入的表达式
      expression: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        jobName: null,
        jobGroup: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        jobName: [
          { required: true, message: "任务名称不能为空", trigger: "blur" },
        ],
        invokeTarget: [
          {
            required: true,
            message: "调用目标字符串不能为空",
            trigger: "blur",
          },
        ],
        cronExpression: [
          {
            required: true,
            message: "cron执行表达式不能为空",
            trigger: "blur",
          },
        ],
      },
    };
  },
  created() {
    this.getDictList();
    this.getList();
  },
  methods: {
    /** 获取定时任务相关的字典数据 */
    getDictList() {
      let params = ["sys_job_group", "sys_job_status", "sys_job_misfire"];
      getDataByDictType(params)
        .then((response) => {
          let dictMap = response.data;
          this.jobDictList = dictMap.sys_job_group.list;
          this.jobDictDefaultValue = dictMap.sys_job_group.defaultValue;
          this.jobStatusList = dictMap.sys_job_status.list;
          this.jobStatusDefaultValue = dictMap.sys_job_status.defaultValue;
          this.jobMisfireList = dictMap.sys_job_misfire.list;
          this.jobMisfireDefaultValue = dictMap.sys_job_misfire.defaultValue;
        })
        .catch((err) => {
          console.error(err);
        });
    },
    /** 查询定时任务调度列表 */
    getList() {
      this.loading = true;
      listJob(this.queryParams).then((response) => {
        this.jobList = response.records;
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
        jobId: null,
        jobName: null,
        jobGroup: null,
        invokeTarget: null,
        cronExpression: null,
        misfirePolicy: null,
        concurrent: null,
        status: 0,
        remark: null,
        createTime: null,
        updateTime: null,
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
      this.ids = selection.map((item) => item.jobId);
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加定时任务调度";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const jobId = row.jobId || this.ids;
      getJob(jobId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改定时任务调度";
      });
    },
    /** 修改任务状态 */
    handleStatusChange(row) {
      change(row)
        .then((res) => {
          if (row.status === 1) {
            this.$modal.msgSuccess("任务已开启");
          } else {
            this.$modal.msgSuccess("任务已暂停");
          }
        })
        .catch((err) => {
          console.error(err);
          setTimeout(() => {
            row.status = row.status === 1 ? 0 : 1;
          }, 1000);
        });
    },
    /** 立即执行一次 */
    handleRun(row) {
      this.$modal
        .confirm('确认要立即执行一次"' + row.jobName + '"任务吗？')
        .then(function () {
          return run(row);
        })
        .then(() => {
          this.$modal.msgSuccess("执行成功");
        })
        .catch(() => {
          this.$message.error(err);
        });
    },
    /** 查看任务详细 */
    handleView(row) {
      getJob(row.jobId).then((response) => {
        this.form = response.data;
        this.openView = true;
      });
    },
    /** 查看日志功能 */
    handleJobLog(row) {
      const jobId = row.jobId || null;
      this.$router.push({ path: "/jobLog", query: { jobId: jobId } });
    },
    /** 更多操作触发*/
    handleCommand(command, row) {
      switch (command) {
        case "handleRun":
          this.handleRun(row);
          break;
        case "handleView":
          this.handleView(row);
          break;
        case "handleJobLog":
          this.handleJobLog(row);
          break;
        default:
          break;
      }
    },
    /** cron表达式按钮操作 */
    handleShowCron() {
      this.expression = this.form.cronExpression;
      this.openCron = true;
    },
    /** Cron表达式确定之后设置的值 */
    crontabFill(value) {
      this.form.cronExpression = value;
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["dataForm"].validate((valid) => {
        if (valid) {
          if (this.form.jobId != null) {
            updateJob(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addJob(this.form).then((response) => {
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
      const jobIds = row.jobId || this.ids;
      this.$modal
        .confirm('是否确认删除定时任务调度编号为"' + jobIds + '"的数据项？')
        .then(function () {
          return delJob(jobIds);
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
