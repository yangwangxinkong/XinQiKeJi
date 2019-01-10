## vue-pc

> pc项目模板

## Build Setup

``` bash
# install dependencies
npm install

# serve with hot reload at localhost:8080
npm run dev

# build for production with minification
npm run build

# build for production and view the bundle analyzer report
npm run build --report
```

## 项目描述
>该项目是由vue-cli项目模板扩展而来，主要集成了 vue、vue-router、vuex、axios、nprogress、异步组件等主要功能（待完善）

**本项目的定位是vue项目基础模板**
**该项目不支持低版本游览器，有需求请自行添加[polyfill详情](https://github.com/PanJiaChen/vue-element-admin/wiki#babel-polyfill)**

## 开发
```bash
// 克隆项目
git clone https://github.com/PanJiaChen/vue-element-admin.git

// 安装依赖
npm install
// or 建议不要用cnpm  安装有各种诡异的bug 可以通过如下操作解决npm速度慢的问题
npm install --registry=https://registry.npm.taobao.org

// 本地开发 开启服务
npm run dev
```
浏览器访问 http://localhost:8080

#### 解决跨域
我们目前的开发都是建立在前后端分离基础上，因此跨域问题也是急切需要解决的，vue-cli 中已经为我们考虑到这一点，在`config/index.js`里面在dev编译环境下有proxyTable属性可以配置:

```
// [http-proxy-middleware](https://github.com/chimurai/http-proxy-middleware#http-proxy-middleware)
proxyTable: {
  '/api': {                         // 匹配以 /api 开头的
    target: 'http://xxxx.com/api',  // 代理的路径  
    changeOrigin: true,             //开启代理
    pathRewrite: {
      '^/api': ''                   //这里重写路径/api就代理到对应路径，不需要可省略
    }
  }
}
```

## 目录结构

```shell
├── build                      // 构建相关  
├── config                     // 配置相关
├── src                        // 源代码
│   ├── assets                 // 主题 字体 图片等静态资源
│   ├── common                 // 全局js css等
│   ├── components             // 全局公用组件
│   ├── directive              // 全局指令
│   ├── router                 // 路由(vue-router)
│   ├── store                  // 全局store管理(vuex)
│   ├── utils                  // 全局公用方法
│   ├── vendor                 // 第三方库
│   ├── views                  // views
│   ├── App.vue                // 入口页面
│   └── main.js                // 入口 加载组件 初始化等
├── static                     // 第三方不打包资源
│   ├── favicon.ico            // favicon图标
│   ├── jquery
├── .babelrc                   // babel-loader 配置
├── eslintrc.js                // eslint 配置项
├── .gitignore                 // git 忽略项
├── index.html                 // html模板
└── package.json               // package.json
```

## 开发规范

#### 代码检测
>使用v `eslint` 进行代码规范检测，用以规范团队代码风格，采用 vue-cli 默认 `JavaScript standard`代码规范 ，[详情请点击](https://github.com/standard/standard/blob/master/docs/RULES-zhcn.md)。

```bash
// 采用标准代码检测规范（https://github.com/feross/standard/blob/master/RULES.md#javascript-standard-style）
module.exports = {
    extends: 'standard'
}
```

#### 命名规范
[web 开发命名规范](http://www.jianshu.com/p/917c04d3b139)
