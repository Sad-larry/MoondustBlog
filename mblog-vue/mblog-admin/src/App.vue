<template>
    <router-view />
</template>

<script>
import ls from "@/utils/sessionStorageUtil.js";
import { organizeMenu } from "@/utils/menuToTree.js";

export default {
    name: "App",
    created() {
        // 刷新页面重新渲染菜单
        // 已经获取过菜单则执行
        if (this.$store.state.activeProgressEnum === 1) {
            // 原来vuex才是插件帮忙保存的会话key
            let vuex = ls.getItem("vuex");
            if (vuex) {
                // 拿出菜单栏数据, 从sessionStorage中重新获取menuList数据
                let userMenuTree = vuex["userMenuTree"];
                if (userMenuTree && userMenuTree.length > 0) {
                    organizeMenu();
                }
            }
        }
    },
};
</script>
<style>
#app {
    font-family: "Avenir", Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 60px;
}
</style>
