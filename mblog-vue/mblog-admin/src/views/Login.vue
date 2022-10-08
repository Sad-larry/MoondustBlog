<template>
    <div class="login-container">
        <div class="login-card">
            <div class="login-title">管理员登录</div>
            <!-- 登录表单 -->
            <el-form
                status-icon
                :model="loginForm"
                :rules="rules"
                ref="ruleForm"
                class="login-form"
            >
                <!-- 用户名输入框 -->
                <el-form-item prop="userName">
                    <el-input
                        v-model="loginForm.userName"
                        prefix-icon="el-icon-user-solid"
                        placeholder="用户名"
                        @keyup.enter.native="login"
                    />
                </el-form-item>
                <!-- 密码输入框 -->
                <el-form-item prop="password">
                    <el-input
                        v-model="loginForm.password"
                        prefix-icon="iconfont el-icon-mymima"
                        show-password
                        placeholder="密码"
                        @keyup.enter.native="login"
                    />
                </el-form-item>
            </el-form>
            <!-- 登录按钮 -->
            <el-button type="primary" @click="login">登录</el-button>
        </div>
    </div>
</template>

  <script>
// import { generaMenu } from "../../assets/js/menu";
import { adminLogin } from "@/api/admin/login"
import { organizeMenu } from "@/utils/menuToTree"
import ls from '@/utils/sessionStorageUtil.js'
export default {
    data: function () {
        return {
            loginForm: {
                userName: "md",
                password: "1",
            },
            rules: {
                userName: [
                    {
                        required: true,
                        message: "用户名不能为空",
                        trigger: "blur",
                    },
                ],
                password: [
                    {
                        required: true,
                        message: "密码不能为空",
                        trigger: "blur",
                    },
                ],
            },
        };
    },
    methods: {
        login() {
            if (this.$store.state.activeProgressEnum === 1) {
                // 原来vuex才是插件帮忙保存的会话key
                let vuex = ls.getItem("vuex");
                if (vuex) {
                    // 拿出菜单栏数据, 从sessionStorage中重新获取menuList数据
                    let userMenuTree = vuex["userMenuTree"];
                    if (userMenuTree && userMenuTree.length > 0) {
                        organizeMenu();
                        this.$message.success("登录成功...");
                        this.$router.push({ path: "/" });
                        return
                    }
                }
            }

            // 表单校验
            this.$refs.ruleForm.validate((valid) => {
                if (valid) {
                    // 如果通过校验则登录
                    adminLogin(this.loginForm).then(({ data }) => {
                        // 数据成功返回应该保存一些数据，例如token什么的
                        window.localStorage.setItem('token', data.data.token)
                        // 整理
                        organizeMenu(data.data.menuTree);
                        this.$message.success("登录成功...");
                        this.$router.push({ path: "/" });
                    });
                } else {
                    return false;
                }
            });
        },
    },
};
</script>

  <style scoped>
.login-container {
    position: absolute;
    top: 0;
    bottom: 0;
    right: 0;
    left: 0;
    /* background: url(https://aurora-static.oss-cn-hangzhou.aliyuncs.com/static/login.png)
        center center / cover no-repeat; */
}
.login-card {
    position: absolute;
    top: 0;
    bottom: 0;
    right: 0;
    background: #fff;
    padding: 170px 60px 180px;
    width: 350px;
}
.login-title {
    color: #303133;
    font-weight: bold;
    font-size: 1rem;
}
.login-form {
    margin-top: 1.2rem;
}
.login-card button {
    margin-top: 1rem;
    width: 100%;
}
</style>
