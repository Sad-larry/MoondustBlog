<template>
  <div class="app-container">
    <el-form v-show="showSearch" :inline="true" ref="form" :model="queryParams" label-width="68px">
      <el-form-item label="任务名称">
        <el-input style="width: 200px" size="small" v-model="queryParams.jobName" placeholder="请输入任务名称" />
      </el-form-item>
      <el-form-item label="任务组名">
        <el-select
          size="small"
          @change="handleFind"
          clearable
          style="margin-left: 5px"
          v-model="queryParams.jobGroup"
          placeholder="请选择组名"
        >
          <el-option v-for="item in jobDictList" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="执行状态">
        <el-select
          size="small"
          @change="handleFind"
          clearable
          style="margin-left: 5px"
          v-model="queryParams.status"
          placeholder="请选择任务状态"
        >
          <el-option value="1" label="成功" />
          <el-option value="0" label="失败" />
        </el-select>
      </el-form-item>
      <el-form-item label="执行时间">
        <el-date-picker
          size="small"
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd HH:mm:ss"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleFind">查找</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          @click="handleDelete"
          v-hasPermi="['system:jobLog:delete']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          @click="handleClean"
          v-hasPermi="['system:jobLog:clean']"
        >清空</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-back" size="mini" @click="handleClose">返回</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!--表格-->
    <el-table v-loading="loading" :data="jobLogList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="日志编号" width="80" align="center" prop="id">
        <template slot-scope="scope">{{ scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column label="任务名称" align="center" prop="jobName" :show-overflow-tooltip="true" />
      <el-table-column label="任务组名" align="center" prop="jobGroup" :show-overflow-tooltip="true">
        <template slot-scope="scope" v-for="(item, index) in jobDictList">
          <el-tag :type="item.style" v-if="scope.row.jobGroup === item.value" :key="index">{{ item.label }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="调用目标字符串" align="center" prop="invokeTarget" :show-overflow-tooltip="true" />
      <el-table-column label="日志信息" align="center" prop="jobMessage" :show-overflow-tooltip="true" />
      <el-table-column label="执行状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="success">成功</el-tag>
          <el-tag v-if="scope.row.status === '1'" type="danger">失败</el-tag>
        </template>
      </el-table-column>
      <el-table-column sortable label="执行时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.startTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">详细</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--分页区域-->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @size-change="getList"
    />

    <!-- 调度日志详细 -->
    <el-dialog title="调度日志详细" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" label-width="100px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="日志序号：">{{ form.id }}</el-form-item>
            <el-form-item label="任务名称：">{{ form.jobName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务分组：">{{ form.jobGroup }}</el-form-item>
            <el-form-item label="执行时间：">{{ form.startTime }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="调用方法：">{{ form.invokeTarget }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="日志信息：">{{ form.jobMessage }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="执行状态：">
              <div v-if="form.status === '0'">成功</div>
              <div v-else-if="form.status === '1'">失败</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="异常信息：" v-if="form.status === 1">{{ form.exceptionInfo }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listJobLog, delJobLog, cleanJobLog } from "@/api/system/jobLog";
import { getDataByDictType } from "@/api/system/dictData";

export default {
  name: "JobLog",
  data() {
    return {
      // 加载层信息
      loading: true,
      // 选中数组
      ids: [],
      jobDictList: [],
      jobDictDefaultValue: null,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 调度日志表格数据
      jobLogList: [],
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 表单参数
      form: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        startTime: null,
        stopTime: null,
        jobId: null,
        jobName: null,
        jobGroup: null,
        status: null,
      },
    };
  },
  created() {
    let jobId = this.$route.query.jobId;
    if (jobId) {
      this.queryParams.jobId = jobId;
    }
    this.getDictList();
    this.getList();
  },
  methods: {
    getDictList() {
      let dictTypeList = ["sys_job_group"];
      getDataByDictType(dictTypeList).then((response) => {
        let dictMap = response.data;
        this.jobDictList = dictMap.sys_job_group.list;
        this.jobDictDefaultValue = dictMap.sys_job_group.defaultValue;
      });
    },
    /** 查询调度日志列表 */
    getList() {
      listJobLog(this.queryParams)
        .then((response) => {
          this.jobLogList = response.records;
          this.total = response.total;
          this.loading = false;
        })
        .catch((err) => {
          console.log(err);
          this.loading = false;
        });
    },
    // 返回按钮
    handleClose() {
      this.$router.push({ path: "/job" });
    },
    /** 搜索按钮操作 */
    handleFind() {
      this.queryParams.pageNum = 1;
      if (this.dateRange.length) {
        this.queryParams.startTime = this.dateRange[0];
        this.queryParams.stopTime = this.dateRange[1];
      }
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.queryParams.startTime = null;
      this.queryParams.stopTime = null;
      this.queryParams.jobName = null;
      this.queryParams.jobGroup = null;
      this.queryParams.status = null;
      this.getList();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
    },
    /** 详细按钮操作 */
    handleView(row) {
      this.open = true;
      this.form = row;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm("此操作将永久删除该记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delJobLog(this.ids)
            .then((res) => {
              this.$message.success("OK");
              this.getList();
            })
            .catch((err) => {
              console.error(err);
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消删除",
          });
        });
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$confirm("是否确认清空所有调度日志数据项?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          cleanJobLog()
            .then((res) => {
              this.$message.success("OK");
              this.getList();
            })
            .catch((err) => {
              console.error(err);
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消清空",
          });
        });
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val;
      this.getList();
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val;
      this.getList();
    },
  },
};
</script>
