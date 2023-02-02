<template>
    <div class="app-container">
        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5" :offset="23">
                <el-tooltip class="item" effect="dark" content="刷新" placement="top">
                    <el-button size="mini" circle icon="el-icon-refresh" @click="refresh()" />
                </el-tooltip>
            </el-col>
        </el-row>

        <el-table border v-loading="loading" :data="onlineUserList" style="width: 100%;">
            <el-table-column align="center" prop="avatar" label="头像" width="180">
                <template slot-scope="scope">
                    <div class="block"><el-avatar :size="50" :src="scope.row.avatar"></el-avatar></div>
                </template>
            </el-table-column>
            <el-table-column align="center" prop="nickname" label="昵称" width="180" />
            <el-table-column align="center" prop="ip" label="IP地址" />
            <el-table-column align="center" prop="city" label="登录地址" />
            <el-table-column align="center" prop="browser" label="浏览器" />
            <el-table-column align="center" prop="os" label="操作系统" />
            <el-table-column align="center" prop="loginTime" label="登录时间" width="180" />
            <el-table-column align="center" label="操作">
                <template slot-scope="scope">
                    <el-button type="danger" size="mini" @click="kick(scope.row)">强制下线</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize" @pagination="getList" />
    </div>
</template>
<script>
import { listOnlineUsers, kick } from "@/api/system/user";

export default {
    name: "AdminLog",
    data() {
        return {
            // 遮罩层
            loading: true,
            // 总条数
            total: 0,
            // 操作日志表格数据
            onlineUserList: [],
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10
            },
        }
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询操作日志列表 */
        getList() {
            this.loading = true;
            listOnlineUsers().then(response => {
                this.onlineUserList = response.records;
                this.total = response.total;
                this.loading = false;
            }).catch(err => {
                this.loading = false;
            });
        },
        /** 刷新操作日志列表 */
        refresh() {
            this.getList();
        },
        /** 强制用户下线 */
        kick(row) {
            kick(row.userUid).then(response => {
                this.$modal.msgSuccess("成功下线该用户");
                this.refresh();
            }).catch(err => {
                this.$modal.msgError("操作失败");
            });
        }
    }
}
</script>
