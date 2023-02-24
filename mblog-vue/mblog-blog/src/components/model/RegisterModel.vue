<template>
  <v-dialog v-model="registerFlag" :fullscreen="isMobile" max-width="460">
    <v-card class="login-container" style="border-radius:4px;">
      <v-icon class="float-right" @click="registerFlag = false">mdi-close</v-icon>
      <div class="login-wrapper">
        <!-- 用户名 -->
        <v-text-field v-model="username" label="邮箱号" placeholder="请输入您的邮箱号" clearable @keyup.enter="register" />
        <!-- 密码 -->
        <v-text-field
          v-model="password"
          class="mt-7"
          label="密码"
          placeholder="请输入您的密码"
          @keyup.enter="register"
          :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
          :type="show ? 'text' : 'password'"
          @click:append="show = !show"
        />
        <!-- 验证码 -->
        <div class="mt-7 send-wrapper">
          <v-text-field maxlength="6" v-model="mailCode" label="验证码" placeholder="请输入6位验证码" @keyup.enter="register" />
          <v-btn :disabled="flag" class="ma-1" color="info" @click="sendCode">{{ codeMsg }}</v-btn>
        </div>
        <!-- 注册按钮 -->
        <v-btn class="mt-7" block color="red" style="color:#fff" @click="register">注册</v-btn>
        <!-- 登录 -->
        <div class="mt-9 login-tip">
          已有账号？
          <span @click="openLogin">登录</span>
        </div>
      </div>
    </v-card>
  </v-dialog>
</template>

<script>
import { userRegister } from "@/api";
export default {
  data() {
    return {
      username: "",
      password: "",
      mailUuid: "",
      mailCode: "",
      loginType: 1,
      flag: true,
      codeMsg: "发送",
      time: 60,
      show: false,
    };
  },
  computed: {
    registerFlag: {
      set(value) {
        this.$store.state.registerFlag = value;
      },
      get() {
        return this.$store.state.registerFlag;
      },
    },
    isMobile() {
      const clientWidth = document.documentElement.clientWidth;
      if (clientWidth > 960) {
        return false;
      }
      return true;
    },
  },
  watch: {
    username(value) {
      var reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
      if (reg.test(value)) {
        this.flag = false;
      } else {
        this.flag = true;
      }
    },
  },
  methods: {
    openLogin() {
      this.$store.state.registerFlag = false;
      this.$store.state.loginFlag = true;
    },
    countDown() {
      this.flag = true;
      this.timer = setInterval(() => {
        this.time--;
        this.codeMsg = this.time + "s";
        if (this.time <= 0) {
          clearInterval(this.timer);
          this.codeMsg = "发送";
          this.time = 60;
          this.flag = false;
        }
      }, 1000);
    },
    sendCode() {
      var reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
      if (!reg.test(this.username)) {
        this.$toast({ type: "error", message: "邮箱格式不正确" });
        return false;
      }
      if (this.password.trim().length < 6) {
        this.$toast({ type: "error", message: "密码不能少于6位" });
        return false;
      }
      const user = {
        username: this.username,
        password: this.password,
        mailUuid: this.mailUuid,
        mailCode: this.mailCode,
        loginType: this.loginType,
      };
      userRegister(user).then((res) => {
        this.mailUuid = res.mailUuid;
        this.time = res.countdown;
        this.countDown();
        this.$toast({ type: "success", message: "邮箱验证码发送成功" });
      });
    },
    register() {
      var reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
      if (!reg.test(this.username)) {
        this.$toast({ type: "error", message: "邮箱格式不正确" });
        return false;
      }
      if (this.password.trim().length < 6) {
        this.$toast({ type: "error", message: "密码不能少于6位" });
        return false;
      }
      if (this.mailCode.trim().length !== 6) {
        this.$toast({ type: "error", message: "请输入6位验证码" });
        return false;
      }
      const user = {
        username: this.username,
        password: this.password,
        mailUuid: this.mailUuid,
        mailCode: this.mailCode,
        loginType: this.loginType,
      };
      userRegister(user).then((res) => {
        // 这个是为了刷新时，重复发送验证码（突然发现这问题真的大）
        if (res.countdown) {
          this.mailUuid = res.mailUuid;
          this.time = res.countdown;
          this.countDown();
        } else {
          // this.$store.commit("login", res.data);
          this.$store.commit("closeModel");
          this.$toast({ type: "success", message: "注册成功" });
        }
      });
    },
  },
};
</script>

<style scoped>
</style>
