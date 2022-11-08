<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px"
      @submit.native.prevent>
      <el-form-item label="反馈类型">
        <el-select  size="small" v-model="queryParams.type" filterable clearable reserve-keyword
                   @change='handleQuery' placeholder="请选择反馈类型"
        >
          <el-option :key="1" label="需求" :value="1"/>
          <el-option :key="2" label="反馈" :value="2"/>
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
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border v-loading="loading" :data="feedBackList" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection"  width="40" align="center"/>
        <el-table-column prop="type" align="center"  label="反馈类型">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.type === 1" type="success">需求</el-tag>
            <el-tag v-else type="danger">缺陷</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="email" align="center"  width="130" label="联系邮箱" />
        <el-table-column prop="title" align="center" width="180" label="需求标题" />
        <el-table-column prop="content" align="center" width="180" label="详细内容" />
        <el-table-column prop="imgUrl" width="160" align="center" label="附加图片" />
        <el-table-column prop="createTime" width="150" align="center" label="反馈时间" >
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
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
  </div>
</template>

<script>
import { listBack, delBack } from "@/api/system/feedback";

export default {
  name: "Back",
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
      // 用户反馈表格数据
      feedBackList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        type: null,
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户反馈列表 */
    getList() {
      this.loading = true;
      listBack(this.queryParams).then(response => {
        this.feedBackList = response.data.records;
        this.total = response.data.total;
        this.loading = false;
      });
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
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除用户反馈编号为"' + ids + '"的数据项？').then(function() {
        return delBack(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
  }
};
</script>
