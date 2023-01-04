<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除
        </el-button>
      </el-col>
      <el-col :span="1.5" :offset="21">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="refresh()" />
        </el-tooltip>
      </el-col>
    </el-row>

    <el-table border v-loading="loading" :data="commentList" @selection-change="handleSelectionChange"
      style="width: 100%;">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="avatar" align="center" width="80" label="头像">
        <template slot-scope="scope">
          <el-avatar shape="square" :size="50" :src="scope.row.avatar"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="nickname" align="center" width="100" label="评论用户" />
      <el-table-column prop="replyNickname" align="center" width="100" label="回复用户" />
      <el-table-column prop="articleTitle" align="center" width="160" label="所属文章" />
      <el-table-column prop="content" align="center" label="内容" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <span v-html="scope.row.content" class="comment-content" />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" width="150" align="center" label="评论时间">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="80" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      ation="getList" />
  </div>
</template>

<script>
import { listComment, delComment } from "@/api/system/comment";

export default {
  name: "Comment",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 评论表格数据
      commentList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询评论列表 */
    getList() {
      this.loading = true;
      listComment(this.queryParams).then(response => {
        this.commentList = response.data.records;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 刷新评论列表 */
    refresh() {
      this.getList();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.multiple = !selection.length
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除评论编号为"' + ids + '"的数据项？').then(function () {
        return delComment(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
  }
};
</script>
