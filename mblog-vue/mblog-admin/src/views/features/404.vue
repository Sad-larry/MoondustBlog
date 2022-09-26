<template>
    <div class="wscn-http404-container">
        <div class="wscn-http404">
            <div class="bullshit">
                <div class="bullshit__oops">OOPS!</div>
                <div class="bullshit__headline">{{ message }}</div>
                <div class="bullshit__info">
                    请检查您输入的网址是否正确，请点击以下按钮返回主页或者发送错误报告
                </div>
                <a href="/" class="bullshit__return-home">返回首页</a>
            </div>
        </div>
    </div>
</template>
  
  <script>
export default {
    name: "Page404",
    computed: {
        message() {
            return "网管说这个页面你不能进......";
        },
    },
};
</script>
  
<style scoped>
.wscn-http404-container {
    transform: translate(-50%, -50%);
    position: absolute;
    top: 40%;
    left: 50%;
}
</style>
  