<template>
  <div class="register">
    <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
      <h3 class="title">月尘博客后台管理系统</h3>
      <el-form-item prop="username">
        <el-input 
        v-model="registerForm.username"
         type="text" 
         auto-complete="off" 
         placeholder="邮箱账号"
         clearable>
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="registerForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter.native="handleRegister"
          clearable
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input
          v-model="registerForm.confirmPassword"
          type="password"
          auto-complete="off"
          placeholder="确认密码"
          @keyup.enter.native="handleRegister"
          clearable
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input
          v-model="registerForm.mailCode"
          auto-complete="off"
          placeholder="邮箱验证码"
          style="width: 63%"
          @keyup.enter.native="handleRegister"
          clearable
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="send-code">
          <el-button
            @click="sendCode"
            :type="isSendCode ? 'info' : 'success'"
            class="send-code-btn"
            :disabled="isSendCode"
            :plain="isSendCode"
          >{{sendMsg}}</el-button>
        </div>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleRegister"
        >
          <span v-if="!loading">注 册</span>
          <span v-else>注 册 中...</span>
        </el-button>
        <div style="float: right;">
          <router-link class="link-type" :to="'/login'">使用已有账户登录</router-link>
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-register-footer">
      <span>
        Copyright © 2023
        <a href="https://refrainblog.cn" target="_blank">refrainblog.cn</a> All Rights Reserved.
      </span>
    </div>
  </div>
</template>

<script>
import { sendMailCode, register } from "@/api/login";

export default {
  name: "Register",
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.registerForm.password !== value) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    return {
      isSendCode: false,
      sendMsg: "发送验证码",
      time: 60,
      registerForm: {
        username: "",
        password: "",
        confirmPassword: "",
        mailCode: "",
      },
      registerRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的邮箱账号" },
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"],
          },
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" },
          {
            min: 6,
            max: 20,
            message: "用户密码长度必须介于 6 和 20 之间",
            trigger: "blur",
          },
        ],
        confirmPassword: [
          { required: true, trigger: "blur", message: "请再次输入您的密码" },
          { required: true, validator: equalToPassword, trigger: "blur" },
        ],
        mailCode: [
          { required: true, trigger: "change", message: "请输入验证码" },
          {
            min: 6,
            max: 6,
            message: "验证码必须为6位",
            trigger: "blur",
          },
        ],
      },
      loading: false,
    };
  },
  methods: {
    /*发送邮件*/
    sendCode() {
      this.$refs.registerForm.validateField("username", (errorMessage) => {
        // 若没有错误信息，则验证成功
        if (!errorMessage) {
          this.countDown();
          sendMailCode( this.registerForm.username ).then((res) => {
            if (res === 200) {
              this.$modal.msgSuccess("邮件发送成功，请注意查收");
            }
          });
        }
      });
    },
    countDown() {
      this.isSendCode = true;
      this.timer = setInterval(() => {
        this.time--;
        this.sendMsg = this.time + "s";
        if (this.time <= 0) {
          clearInterval(this.timer);
          this.sendMsg = "发送验证码";
          this.time = 60;
          this.isSendCode = false;
        }
      }, 1000);
    },
    handleRegister() {
      this.$refs.registerForm.validate((valid) => {
        if (valid) {
          this.loading = true;
          register(this.registerForm)
            .then((res) => {
              const username = this.registerForm.username;
              this.$alert(
                "<font color='red'>恭喜你，您的账号 " +
                  username +
                  " 注册成功！</font>",
                "系统提示",
                {
                  dangerouslyUseHTMLString: true,
                  type: "success",
                }
              )
                .then(() => {
                  this.$router.push("/login");
                })
                .catch(() => {});
            })
            .catch(() => {
              this.loading = false;
            });
        }
      });
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss">
.register {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/login-background.jpg");
  background-size: cover;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.register-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.register-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.send-code {
  float: right;
}
.send-code-btn {
  width: 115px;
  height: 38px;
}
.el-register-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
</style>
