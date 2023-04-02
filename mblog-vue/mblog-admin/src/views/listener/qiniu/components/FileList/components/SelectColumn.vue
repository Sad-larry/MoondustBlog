<template>
  <div class="select-column-wrapper">
    <!-- <el-dropdown trigger="click" @command="handleCommand">
      <el-button type="info" size="mini" plain icon="el-icon-s-operation" @click="selectColumnBtn">设置显示列</el-button>
      <el-dropdown-menu slot="dropdown">
        <el-checkbox-group v-model="checkboxSelectColumnList">
          <el-checkbox v-for="item in columnOptions" :key="item.value" :label="item.value">{{ item.label }}</el-checkbox>
          <el-dropdown-item :command="item.value"></el-dropdown-item>
        </el-checkbox-group>
      </el-dropdown-menu>
    </el-dropdown>-->

    <el-dropdown trigger="click" @visible-change="onVisibleChange" class="dropdown">
      <el-button type="info" size="mini" plain icon="el-icon-s-operation" @click="selectColumnBtn">设置显示列</el-button>
      <el-dropdown-menu slot="dropdown" :hide-on-click="false">
        <div class="drop">
          <!-- 全选按钮 -->
          <el-checkbox
            :indeterminate="isIndeterminate"
            v-model="checkAll"
            @change="handleCheckAllChange"
            class="checkAlls"
          >全选</el-checkbox>
          <div class="checkAllLine"></div>
          <el-checkbox-group v-model="isCheckIdList" @change="handleCheckedChange">
            <div class="checkboxLists">
              <el-checkbox
                v-for="item in checkboxLists"
                :label="item.value"
                :key="item.value"
                style="display: block"
                class="checiboxItem"
              >{{ item.label }}</el-checkbox>
            </div>
          </el-checkbox-group>
          <div class="footer">
            <el-button type="primary" plain size="mini" class="footer_close">关 闭</el-button>
            <el-button type="primary" size="mini" class="footer_sure" @click="commitSelectColumn">确 定</el-button>
          </div>
        </div>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "SelectColumn",
  props: {},
  data() {
    return {
      checkAll: false,
      // 半选状态
      isIndeterminate: false,
      // 菜单集合
      checkboxLists: [
        {
          value: "extension",
          label: "类型",
        },
        {
          value: "fileSize",
          label: "大小",
        },
        {
          value: "createTime",
          label: "创建日期",
        },
      ],
      isCheckIdList: [], // 选中的

      columnOptions: [],
      checkboxSelectColumnList: [],
    };
  },
  computed: {
    selectedColumnList() {
      return this.$store.getters.selectedColumnList;
    },
  },
  methods: {
    /** 是否显示菜单列表 */
    onVisibleChange(v) {
      if (!v) {
        // 关闭后
        this.commitSelectColumn();
      }
    },
    /** 打开菜单筛选列按钮 */
    selectColumnBtn() {
      this.isCheckIdList = [...this.selectedColumnList];
      // 全选状态
      this.handleCheckedChange(this.isCheckIdList)
    },
    /** 全选按钮 */
    handleCheckAllChange(val) {
      let idList = this.checkboxLists.map((item) => item.value);
      this.isCheckIdList = val ? idList : [];
      this.isIndeterminate = false;
    },
    /** 选择 某一项 */
    handleCheckedChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.checkboxLists.length;
      this.isIndeterminate =
        checkedCount > 0 && checkedCount < this.checkboxLists.length;
    },
    /** 选择确定 提交选择列 */
    commitSelectColumn() {
      this.$store.commit("SET_SELECTED_COLUMN_LIST", this.isCheckIdList);
    },
  },
};
</script>

<style lang="scss" scoped>
.dropdown {
  cursor: pointer;
}
.drop {
  padding: 10px;
}
.input {
  width: 100px;
  margin: 0 auto;
  width: 100%;
}
.checkAlls {
  margin-top: 5px;
  padding-bottom: 5px;
  display: block;
}
.checkAllLine {
  width: 75%;
  border-bottom: 1px solid #e4e4e4;
  margin: 0 auto;
}
.checkboxLists {
  max-height: 260px;
  padding-right: 10px;
  overflow-y: scroll;
  .checiboxItem {
    margin: 3px 0;
  }
}
.noData {
  margin: 20px 0;
  font-size: 14px;
  color: #ccc;
  width: 100%;
  text-align: center;
}
.footer {
  margin-top: 15px;
  width: 100%;
  text-align: center;
  .footer_sure {
    margin-left: 20px;
  }
}
</style>