<template>
    <div class="app-container">
        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
                    @click="handleDelete">批量删除
                </el-button>
            </el-col>
            <el-col :span="1.5" :offset="20">
                <el-tooltip class="item" effect="dark" content="刷新" placement="top">
                    <el-button size="mini" circle icon="el-icon-refresh" @click="refresh()" />
                </el-tooltip>
            </el-col>
        </el-row>

        <el-table border v-loading="loading" :data="userLogList" @selection-change="handleSelectionChange"
            style="width: 100%;">
            <el-table-column align="center" type="selection" width="55" />
            <el-table-column prop="ip" align="center" width="130" label="IP" />
            <el-table-column prop="address" align="center" width="200" label="IP来源" />
            <el-table-column prop="accessOs" align="center" label="平台" />
            <el-table-column prop="browser" align="center" label="浏览器" />
            <el-table-column prop="type" align="center" label="操作类型" />
            <el-table-column prop="description" align="center" label="操作日志" />
            <el-table-column prop="model" align="center" label="操作模块" />
            <el-table-column prop="result" align="center" label="操作结果" />
            <el-table-column prop="createTime" align="center" width="200" sortable label="操作时间" />
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize" @pagination="getList" />
    </div>
</template>
<script>
import { listUserLog, delUserLog } from "@/api/system/userLog";

export default {
    name: "UserLog",
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
            // 操作日志表格数据
            userLogList: [],
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
            listUserLog(this.queryParams).then(response => {
                this.userLogList = response.data.records;
                this.total = response.data.total;
                this.loading = false;
            }).catch(err => {
                this.loading = false;
            });
        },
        /** 刷新操作日志列表 */
        refresh() {
            this.getList();
        },
        /** 多选框选中数据 */
        handleSelectionChange(selection) {
            this.ids = selection.map(item => item.id)
            this.multiple = !selection.length
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const ids = row.id || this.ids;
            this.$modal.confirm('是否确认删除操作日志编号为"' + ids + '"的数据项？').then(function () {
                return delUserLog(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
    }
}
</script>
