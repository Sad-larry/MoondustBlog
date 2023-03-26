/**
* v-hasPermi 操作权限处理
* Copyright (c) 2019 ruoyi
*/

import store from '@/store'

export default {
  inserted(el, binding, vnode) {
    const { value } = binding
    const all_permission = "*:*:*";
    const permissions = store.getters && store.getters.permissions

    if (value && value instanceof Array && value.length > 0) {
      const permissionFlag = value

      const hasPermissions = permissions.some(permission => {
        return all_permission === permission || permissionFlag.includes(permission)
      })

      if (!hasPermissions) {
        // 若无权限则直接禁用按钮，而不是隐藏按钮，否则这会让界面布局显示非常的难看
        // 设置禁用属性后，不允许在组件上再创建disabled数据双向绑定，否则将会影响按钮的禁用状态功能
        // el.parentNode && el.parentNode.removeChild(el)
        // element-ui el-button 禁用属性
        el.disabled = true;
        // element-ui el-button 类名，添加禁用样式
        el.classList.add('is-disabled');
      }
    } else {
      throw new Error(`请设置操作权限标签值`)
    }
  },
}
