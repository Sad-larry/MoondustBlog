<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item label="用户昵称" prop="nickname">
      <el-input v-model="user.nickname" maxlength="30" />
    </el-form-item>
    <el-form-item label="个人介绍" prop="intro">
      <el-input v-model="user.intro" type="textarea" :autosize="{ minRows: 2, maxRows: 8}" maxlength="255" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit" v-hasPermi="['system:userProfile:update']">保存</el-button>
      <el-button type="danger" size="mini" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserProfile } from "@/api/system/user";

export default {
  props: {
    user: {
      type: Object,
    },
  },
  data() {
    return {
      // 表单校验
      rules: {
        nickname: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" },
        ],
        intro: [
          { required: true, message: "个人介绍不能为空", trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    submit() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          updateUserProfile(this.user).then((res) => {
            this.$modal.msgSuccess("修改成功");
          });
        }
      });
    },
    close() {
      this.$tab.closePage();
    },
  },
};
</script>
