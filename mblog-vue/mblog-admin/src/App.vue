<template>
    <router-view />
</template>

<script>
import { organizeMenu } from "@/utils/menuToList.js";
export default {
    name: "App",
    created() {
        // 刷新页面重新渲染菜单
        if (this.$store.state.userInfo != null) {
            organizeMenu(this.$store.state.userMenuList);
        }
    }
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
