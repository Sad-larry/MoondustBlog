<template>
    <div class="weather">
        <div id="he-plugin-standard"></div>
    </div>
</template>         
<script>
// https://widget.qweather.com/
window.WIDGET = {
    "CONFIG": {
        "layout": "2",
        "width": "230",
        "height": "300",
        "background": "5",
        "dataColor": "2F3640",
        "borderRadius": "5",
        "language": "zh",
        "key": "413ecb4d597641a9ad6c778abfa36b4c"
    }
}
export default ({
    mounted() {
        this.$nextTick(() => {
            this.load()
        })
    },
    methods: {
        load() {
            const s = document.createElement('script')
            s.type = 'text/javascript'
            s.src =
                'https://widget.qweather.net/standard/static/js/he-standard-common.js?v=2.0'
            document.body.appendChild(s)
            // XML 解析错误：格式不佳
            // 位置：https://widget-api.heweather.net/s6/plugin/view?key=413ecb4d597641a9ad6c778abfa36b4c&location=CN101241101&lang=zh
            // 行 1，列 1：
            // 我出现了这种情况，但是不知道问题出现在哪里
        },
    },

});
</script>
<style lang="scss" scoped>
.weather {
    width: 60px !important;
    height: 100%;
}
</style>