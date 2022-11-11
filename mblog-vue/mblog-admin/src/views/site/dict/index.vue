<template>
    <div class="app-container">
        <!-- 查询和其他操作 -->
        <el-form v-show="showSearch" :inline="true" ref="form" :model="queryParams" label-width="68px">
            <el-form-item label="字典名称">
                <el-input style="width: 200px" size="small" v-model="queryParams.name" placeholder="请输入字典名称" />
            </el-form-item>
            <el-form-item label="是否发布">
                <el-select v-model="queryParams.isPublish" filterable size="small" clearable reserve-keyword
                    placeholder="是否发布" @change='handleFind'>
                    <el-option v-for="(item) in isPublishList" :key="item.value" :label="item.label"
                        :value="item.value"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="small" @click="handleFind">查找</el-button>
                <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" icon="el-icon-plus" size="small" @click="handleAdd">新增
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button :disabled="!multipleSelection.length" type="danger" icon="el-icon-delete" size="small"
                    @click="handleDeleteBatch">批量删除
                </el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="listDict"></right-toolbar>
        </el-row>


        <div style="margin-top: 5px">
            <el-table border :data="dictList" style="width: 100%" @selection-change="handleSelectionChange"
                :default-sort="{ prop: 'sort', order: 'descending' }">
                <el-table-column align="center" type="selection"></el-table-column>
                <el-table-column label="字典类型" width="200" align="center">
                    <template slot-scope="scope">
                        <el-tag type="warning" style="cursor: pointer">{{ scope.row.type }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="字典名称" width="150" align="center">
                    <template slot-scope="scope">
                        <span>{{ scope.row.name }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="备注" width="250" align="center">
                    <template slot-scope="scope">
                        <span>{{ scope.row.remark }}</span>
                    </template>
                </el-table-column>

                <el-table-column label="发布状态" width="110" align="center" prop="isPublish" sortable="custom"
                    :sort-by="['isPublish']">
                    <template slot-scope="scope">
                        <span v-for="item in isPublishList" :key="item.value">
                            <el-tag :type="item.style" v-if="scope.row.isPublish == item.value">{{ item.label }}
                            </el-tag>
                        </span>
                    </template>
                </el-table-column>

                <el-table-column label="排序" width="80" align="center" prop="sort" sortable="custom"
                    :sort-orders="['ascending', 'descending']">
                    <template slot-scope="scope">
                        <span>{{ scope.row.sort }}</span>
                    </template>
                </el-table-column>

                <el-table-column label="创建时间" width="160" align="center" prop="createTime" sortable="custom"
                    :sort-by="['createTime']">
                    <template slot-scope="scope">
                        <span>{{ scope.row.createTime }}</span>
                    </template>
                </el-table-column>

                <el-table-column label="操作" align="center" min-width="240">
                    <template slot-scope="scope">
                        <el-button @click="handleList(scope.row)" type="success" size="mini">列表</el-button>
                        <el-button @click="handleEdit(scope.row)" type="primary" size="mini">编辑
                        </el-button>
                        <el-button @click="handleDelete(scope.row)" type="danger" size="mini">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <!--分页区域-->
        <div class="pagination-container" style="float: right;margin-bottom: 1.25rem;margin-top: 1.25rem;">
            <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="queryParams.pageNum" :page-size="queryParams.pageSize" :page-sizes="[10, 20, 30]"
                layout="total, sizes,prev, pager, next,jumper" :total="total">
            </el-pagination>
        </div>

        <!-- 添加或修改对话框 -->
        <el-dialog center :title="title" :visible.sync="open">
            <el-form :model="form" :rules="rules" ref="form">

                <el-form-item label="字典类型" :label-width="formLabelWidth" prop="type">
                    <el-input v-model="form.type" auto-complete="off"></el-input>
                </el-form-item>

                <el-form-item label="字典名称" :label-width="formLabelWidth" prop="name">
                    <el-input v-model="form.name" auto-complete="off"></el-input>
                </el-form-item>

                <el-form-item label="发布状态" :label-width="formLabelWidth" prop="isPublish">
                    <el-radio-group v-model="form.isPublish" size="small">
                        <el-radio v-for="(item) in isPublishList" :key="item.value" :label="parseInt(item.value)" border>{{
                                item.label
                        }}
                        </el-radio>
                    </el-radio-group>
                </el-form-item>

                <el-form-item label="备注" :label-width="formLabelWidth">
                    <el-input v-model="form.remark" auto-complete="off"></el-input>
                </el-form-item>

                <el-form-item label="排序" :label-width="formLabelWidth" prop="sort">
                    <el-input v-model="form.sort" auto-complete="off"></el-input>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="open = false">取 消</el-button>
                <el-button type="primary" @click="submitForm">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>
  
<script>
import { listDict, addDict, updateDict, delDict } from "@/api/system/dict"
import { getDataByDictType } from "@/api/system/dictData"

export default {
    data() {
        return {
            query: {}, // 查询对象
            multipleSelection: [], //多选，用于批量删除
            dictList: [],
            isPublishList: [],
            publishDefaultValue: null,
            keyword: "",
            queryParams: {
                name: null,
                pageNum: 1,
                pageSize: 10,
            },
            total: 0, //总数量
            title: "增加字典类型",
            open: false, //控制弹出框
            showSearch: true,
            formLabelWidth: "120px",
            isEditForm: false,
            form: {},
            rules: {
                type: [
                    { required: true, message: '字典类型不能为空', trigger: 'blur' },
                    { min: 1, max: 20, message: '长度在1到20个字符' },
                ],
                name: [
                    { required: true, message: '字典名称不能为空', trigger: 'blur' },
                    { min: 1, max: 20, message: '长度在1到20个字符' },
                ],
                isPublish: [
                    { required: true, message: '发布状态不能为空', trigger: 'blur' }
                ],
                sort: [
                    { required: true, message: '排序字段不能为空', trigger: 'blur' },
                    { pattern: /^[0-9]\d*$/, message: '排序字段只能为自然数' },
                ]
            }
        };
    },
    created() {
        this.getDictDataList()
        this.listDict();
    },
    methods: {
        listDict() {
            listDict(this.queryParams).then(response => {
                this.dictList = response.data.records;
                this.total = response.data.total;
            }).catch(err => {
                console.log(err)
            });
        },
        // 这里可以设置一些初始值
        getFormObject() {
            return {
                isPublish: this.publishDefaultValue,
                sort: 0
            };
        },
        getDictDataList() {
            let dictType = 'sys_publish_status';
            getDataByDictType({ "type": dictType }).then(response => {
                let dictMap = response.data;
                this.isPublishList = dictMap.sys_publish_status.list;
                this.publishDefaultValue = dictMap.sys_publish_status.defaultValue
            }).catch(err => {
                console.error(err)
            })
        },
        resetQuery() {
            this.queryParams.name = null
            this.listDict()
        },
        handleFind() {
            this.queryParams.pageNum = 1
            this.listDict();
        },
        handleAdd() {
            this.title = "增加字典类型"
            this.open = true;
            this.form = this.getFormObject();
            this.isEditForm = false;
        },
        handleEdit(row) {
            this.title = "编辑字典类型";
            this.isEditForm = true;
            this.form = row;
            this.open = true;
        },
        handleDelete(row) {
            let that = this;
            this.$confirm("此操作将把字典类型删除, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                delDict(row.id).then(response => {
                    this.$message.success(response.message)
                    that.listDict();
                }).catch(err => {
                    console.error(err)
                });
            }).catch(() => {
                this.$message.info("已取消删除")
            });
        },
        handleDeleteBatch(row) {
            let that = this;
            if (that.multipleSelection.length <= 0) {
                this.$message.error("请先选中需要删除的内容!")
                return;
            }
            this.$confirm("此操作将把选中字典类型删除, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            })
                .then(() => {
                    let ids = []
                    that.multipleSelection.forEach(item => {
                        ids.push(item.id)
                    })
                    delDict(ids).then(response => {
                        this.$message.success(response.message)
                        that.listDict();
                    }).catch(err => {
                        console.error(err)
                    });
                }).catch(() => {
                    this.$message.info("已取消删除")
                });
        },
        handleList(row) {
            this.$router.push({
                path: "dictData",
                query: { dictId: row.id }
            });
        },
        handleCurrentChange(val) {
            this.queryParams.pageNum = val;
            this.listDict();
        },
        handleSizeChange(val) {
            this.queryParams.pageSize = val
            this.listDict()
        },
        // 改变多选
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        submitForm() {
            this.$refs.form.validate((valid) => {
                if (!valid) {
                    console.log("校验出错")
                } else {
                    if (this.isEditForm) {
                        updateDict(this.form).then(response => {
                            this.$message.success("修改成功")
                            this.open = false;
                            this.listDict();
                        }).catch(err => {
                            console.error(err)
                        });
                    } else {
                        addDict(this.form).then(response => {
                            this.$message.success("添加成功")
                            this.open = false;
                            this.listDict();
                        }).catch(err => {
                            console.error(err)
                        });
                    }
                }
            })
        }
    }
};
</script>
  