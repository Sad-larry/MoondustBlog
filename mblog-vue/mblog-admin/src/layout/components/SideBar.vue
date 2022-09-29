<template>
    <div>
        <el-menu
            class="side-nav-bar"
            router
            :collapse="this.$store.state.collapse"
            :default-active="this.$route.path"
            background-color="#304156"
            text-color="#BFCBD9"
            active-text-color="#409EFF"
        >
            <!-- 获取所有菜单列表 -->
            <template v-for="route of this.$store.state.userMenuList">
                <template v-if="route.menuName">
                    <el-submenu :key="route.path" :index="route.path">
                        <!-- 一级菜单标题 -->
                        <template slot="title">
                            <i :class="route.icon" />
                            <span>{{ route.menuName }}</span>
                        </template>
                        <!-- 二级菜单选项 -->
                        <div
                            v-for="(item, index) of route.subMenuList"
                            :key="index"
                        >
                            <el-menu-item :index="item.path">
                                <i :class="item.icon" />
                                <span slot="title">{{ item.menuName }}</span>
                            </el-menu-item>
                        </div>
                    </el-submenu>
                </template>
            </template>
        </el-menu>
    </div>
</template>
  
<style scoped>
.side-nav-bar:not(.el-menu--collapse) {
    width: 210px;
}
.side-nav-bar {
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
    overflow-x: hidden;
    overflow-y: auto;
}
.side-nav-bar i {
    margin-right: 1rem;
}
*::-webkit-scrollbar {
    width: 0.5rem;
    height: 1px;
}
*::-webkit-scrollbar-thumb {
    border-radius: 0.5rem;
    background-color: rgba(144, 147, 153, 0.3);
}
</style>
  