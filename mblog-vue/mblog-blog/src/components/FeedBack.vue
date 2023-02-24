<template>
  <v-dialog
    @keydown.esc="closeDialog"
    @click:outside="closeDialog"
    max-width="600px"
    transition="dialog-top-transition"
    :value.sync="this.$store.state.dialogFormVisible"
  >
    <v-card>
      <v-card-title>
        <span class="text-h5">留言反馈</span>
      </v-card-title>
      <v-card-text>
        <v-container>
          <v-form ref="dataForm" lazy-validation>
            <v-radio-group v-model="feedback.type" row label="反馈类型" required :rules="rules.type">
              <v-radio :value="1">
                <template v-slot:label>
                  <div>
                    给 {{ blogInfo.webSite.author }}提交
                    <strong class="success--text">需求</strong>
                  </div>
                </template>
              </v-radio>
              <v-radio :value="2">
                <template v-slot:label>
                  <div>
                    给 {{ blogInfo.webSite.author }}提交
                    <strong class="primary--text">缺陷</strong>
                  </div>
                </template>
              </v-radio>
            </v-radio-group>
            <v-text-field v-model="feedback.email" :rules="rules.email" label="联系邮箱" required clearable></v-text-field>
            <v-text-field v-model="feedback.title" :rules="rules.title" :label="titleLabel" required clearable></v-text-field>
            <v-textarea v-model="feedback.content" solo name="内容详细(可不填)" label="内容详细(可不填)" hint="内容详细(可不填)" rows="1"></v-textarea>
          </v-form>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="warning" @click="close">取 消</v-btn>
        <v-btn color="success" @click="submit">确 定</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { addFeedback } from "@/api";
export default {
  data() {
    return {
      feedback: {
        type: 1,
        email: null,
        title: null,
        content: null,
      },
      rules: {
        type: [(v) => !!v || "必填字段"],
        email: [
          (v) => !!v || "填写邮箱",
          (v) => /.+@.+\..+/.test(v) || "填写正确的邮箱",
        ],
        title: [(v) => !!v || "必填字段"],
      },
    };
  },
  computed: {
    blogInfo() {
      return this.$store.state.blogInfo;
    },
    titleLabel() {
      return this.feedback.type === 1 ? "需求" : "缺陷反馈";
    },
  },
  methods: {
    close() {
      this.feedback.type = 1;
      this.feedback.email = null;
      this.feedback.title = null;
      this.feedback.content = null;
      this.closeDialog();
    },
    closeDialog() {
      this.$refs["dataForm"].resetValidation();
      this.$store.commit("setDialogFormVisible");
    },
    submit() {
      if (this.$refs["dataForm"].validate()) {
        addFeedback(this.feedback)
          .then((res) => {
            this.$toast({ type: "success", message: "提交反馈成功" });
            this.close();
          })
          .catch((err) => {
            this.$toast({ type: "error", message: "提交反馈失败" });
          });
      } else {
        this.$toast({ type: "error", message: "存在必填字段未填" });
      }
    },
  },
};
</script>
