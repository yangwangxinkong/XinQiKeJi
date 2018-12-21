'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  ENV_CONFIG: '"dev"',
  BASE_API: '"http://localhost:8082"',
  BASE_UPLOAD: '"http://47.92.228.30/upload-web"'
})
