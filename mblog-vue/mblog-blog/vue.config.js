const path = require('path')
const VuetifyLoaderPlugin = require('vuetify-loader/lib/plugin')

function resolve(dir) {
  return path.join(__dirname, dir)
}

module.exports = {
  publicPath: "/",
  lintOnSave: process.env.NODE_ENV === "development",
  devServer: {
    host: '0.0.0.0',
    port: 80,
    open: true,
    proxy: {
      // detail: https://cli.vuejs.org/config/#devserver-proxy
      [process.env.VUE_APP_BASE_API]: {
        target: `http://localhost:8080`,
        changeOrigin: true,
        pathRewrite: {
          ['^' + process.env.VUE_APP_BASE_API]: ''
        }
      }
    },
    disableHostCheck: true
  },
  configureWebpack: {
    resolve: {
      alias: {
        '@': resolve('src')
      }
    },
    plugins: [
      new VuetifyLoaderPlugin({
        // 通过对每一个原件进行匹配，并自动引入元件，而无需在script标签中引入
        // MoonComponent和moon-component都可自动引入
        match(originalTag, { kebabTag, camelTag, path, component }) {
          if (kebabTag.startsWith('moon-')) {
            return [camelTag, `import ${camelTag} from '@/components/${camelTag}.vue'`]
          }
        }
      })
    ]
  },
};
