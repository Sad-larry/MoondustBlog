// sessionStorage工具类
const ls = window.sessionStorage;
export default {
    // 用来封装数据的获取，其参数是key名
    getItem(key) {
        try {
            return JSON.parse(ls.getItem(key));
        } catch (err) {
            return null;
        }
    },
    // 用来封装设置数据，其参数是key名
    setItem(key, val) {
        ls.setItem(key, JSON.stringify(val));
    },
    // 负责清楚当前所有的sessionStorage数据
    clear() {
        ls.clear();
    },
    // 封装了获取指定下标对应数据的key名，所以参数是index值
    keys(index) {
        return ls.key(index);
    },
    // 负责移除某一个指定key的对应数据
    removeItem(key) {
        ls.removeItem(key);
    }
}