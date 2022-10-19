'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  VUE_APP_BASE_API: '"http://localhost:8080"',
  VUE_APP_TITLE: '"月尘博客管理后台"'
})
