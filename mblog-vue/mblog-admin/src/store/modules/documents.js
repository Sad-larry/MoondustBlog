
const documents = {
  state: {
    // 文件查看模式 0列表模式 1网格模式 2 时间线模式
    currentFileModel: Number(sessionStorage.getItem("FILE_MODEL")) || 0,
    // 包含已浏览的目录路径
    pathTreeMap: JSON.parse(sessionStorage.getItem("PATH_TREE_MAP"), reviver) || new Map(),
    // 文件路径
    filePathList: [],
    // 当前文件路径
    currentFilePath: '',
    // 用户设置的操作列显隐
    selectedColumnList: JSON.parse(sessionStorage.getItem("SELECTED_COLUMN_LIST")) || [],
    //  操作列是否展开
    operaColumnExpand: sessionStorage.getItem("operaColumnExpand") || false,
  },
  mutations: {
    CHANGE_FILE_MODEL: (state, data) => {
      state.currentFileModel = data
      sessionStorage.setItem("FILE_MODEL", data)
    },
    SET_PATH_TREE_MAP: (state, data) => {
      if (data instanceof Array) {
        data.forEach(element => {
          state.pathTreeMap.set(element.id, { "id": element.id, "pid": element.pid, "filename": element.filename })
        });
        sessionStorage.setItem("PATH_TREE_MAP", JSON.stringify(state.pathTreeMap, replacer))
      }
    },
    PUSH_FILE_PATH: (state, data) => {
      state.filePathList.push(data)
    },
    SET_FILE_PATH: (state, data) => {
      state.filePathList = data
    },
    CLEAR_FILE_PATH: (state) => {
      state.filePathList = []
    },
    SET_CURRENT_FILE_PATH: (state, data) => {
      state.currentFilePath = data
    },
    SET_SELECTED_COLUMN_LIST(state, data) {
      if (data instanceof Array) {
        sessionStorage.setItem("SELECTED_COLUMN_LIST", JSON.stringify(data))
        state.selectedColumnList = data
      }
    },
    SET_OPERA_COLUMN_EXPAND: (state, data) => {
      state.operaColumnExpand = data
    },
  },
  actions: {
    fetchPathTreeMap({ commit }, data) {
      commit("SET_PATH_TREE_MAP", data)
    },
    initFileList({ commit }) {
      // 设置根目录以及根路径
      let data = [{ id: "", pid: ".", filename: "根目录" }]
      commit("SET_PATH_TREE_MAP", data)
      commit("SET_FILE_PATH", data)
      commit("SET_CURRENT_FILE_PATH", "")
    },
    setFilePath({ state, commit }, id) {
      if (id !== null) {
        let path = []
        let pid = id;
        while (pid !== '.') {
          let data = state.pathTreeMap.get(pid)
          path.push(data)
          pid = data.pid
        }
        commit("SET_FILE_PATH", path.reverse())
        commit("SET_CURRENT_FILE_PATH", id)
      }
    }
  },
}

/** Map对象JSON序例化构造函数 */
function replacer(key, value) {
  const originalObject = this[key];
  if (originalObject instanceof Map) {
    return {
      dataType: 'Map',
      value: Array.from(originalObject.entries()),
    };
  } else {
    return value;
  }
}

/** Map对象JSON反序例化解析函数 */
function reviver(key, value) {
  if (typeof value === 'object' && value !== null) {
    if (value.dataType === 'Map') {
      return new Map(value.value);
    }
  }
  return value;
}

export default documents
