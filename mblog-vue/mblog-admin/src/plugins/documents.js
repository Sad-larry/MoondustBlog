import store from '@/store'
// 切片最大大小
const CHUNK_SIZE = 1024 * 1024;
// 文件根目录ID
const ROOT_PATH = null;

// 文件展示模式
const FILE_MODEL = {
  // 列表
  TABLE: 0,
  // 网格
  GRID: 1,
  // 时间线
  TIME_LINE: 2
}

// 可识别的图片类型
const IMG_TYPE_LIST = ["jpg", "png", "gif", "jpeg"]


//  文件图片Map映射
const FILE_IMG_MAP = {
  dir: require('@/assets/file/dir.png'),
  png: require('@/assets/file/file_pic.png'),
  jpg: require('@/assets/file/file_pic.png'),
  jpeg: require('@/assets/file/file_pic.png'),
  gif: require('@/assets/file/file_gif.png'),
  unknown: require('@/assets/file/file_unknown.png'),
};

export default {
  CHUNK_SIZE,
  ROOT_PATH,
  FILE_MODEL,
  IMG_TYPE_LIST,
  FILE_IMG_MAP,
  // 文件路径
  filePath() {
    // 类型文件
    return {
      current() {
        return store.getters.currentFilePath
      },
    }
  },
  // 文件查看模式 0列表模式 1网格模式 2 时间线模式
  fileModel() {
    // 是回收站类型文件
    return {
      current() {
        return store.getters.currentFileModel
      },
      isTable() {
        return FILE_MODEL.TABLE === store.getters.currentFileModel
      },
      isGrid() {
        return FILE_MODEL.GRID === store.getters.currentFileModel
      },
      isTimeLine() {
        return FILE_MODEL.TIME_LINE === store.getters.currentFileModel
      }
    }
  },

}