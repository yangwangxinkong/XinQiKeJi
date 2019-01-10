var path = require('path')
var utils = require('./utils')
var config = require('../config')
var vueLoaderConfig = require('./vue-loader.conf')
var webpack = require('webpack')
const SpritesPlugin = require('webpack-spritesmith');

function resolve(dir) {
    return path.join(__dirname, '..', dir)
}

module.exports = {
    // entry: {
    //   app: './src/main.js'
    // },
    entry: utils.entries(),
    output: {
        path: config.build.assetsRoot,
        filename: '[name].js',
        publicPath: process.env.NODE_ENV === 'production' ?
            config.build.assetsPublicPath : config.dev.assetsPublicPath
    },
    resolve: {
        extensions: ['.js', '.vue', '.json'],
        alias: {
            'vue$': 'vue/dist/vue.esm.js',
            '@': resolve('src'),
            'css': '@/common/less',
            'pages': resolve('src/views'),
            'components': resolve('src/components')
        }
    },
    module: {
        rules: [
            // {
            //   test: /\.(js|vue)$/,
            //   loader: 'eslint-loader',
            //   enforce: 'pre',
            //   include: [resolve('src'), resolve('test')],
            //   options: {
            //     formatter: require('eslint-friendly-formatter')
            //   }
            // },
            {
                test: /\.vue$/,
                loader: 'vue-loader',
                options: vueLoaderConfig
            },
            {
                test: /\.js$/,
                loader: 'babel-loader',
                include: [resolve('src'), resolve('test')]
            },
            {
                test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
                loader: 'url-loader',
                options: {
                    limit: 10000,
                    name: utils.assetsPath('img/[name].[hash:7].[ext]')
                }
            },
            {
                test: /\.(mp4|webm|ogg|mp3|wav|flac|aac)(\?.*)?$/,
                loader: 'url-loader',
                options: {
                    limit: 10000,
                    name: utils.assetsPath('media/[name].[hash:7].[ext]')
                }
            },
            {
                test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
                loader: 'url-loader',
                options: {
                    limit: 10000,
                    name: utils.assetsPath('fonts/[name].[hash:7].[ext]')
                }
            }
        ]
    },
    plugins: [
        // 雪碧图相关
        new SpritesPlugin({
            // 目标小图标
            src: {
                cwd: path.resolve(__dirname, '../src/assets/images/icon'),
                glob: '*.png'
            },
            // 输出雪碧图文件及样式文件
            target: {
                image: path.resolve(__dirname, '../src/assets/css/sprite.png'),
                css: [
                    [path.resolve(__dirname, '../src/assets/css/sprite.css'), {
                        //format: 'function_based_template'
                    }]
                ]
            },
            //  customTemplates : {
            //       function_based_template: path.resolve(__dirname, '../sprite_handlebars_template.handlebars')
            //   },
            // 样式文件中调用雪碧图地址写法
            apiOptions: {
                cssImageRef: "~@/assets/css/sprite.png?v=" + Date.parse(new Date())
            },
            spritesmithOptions: {
                algorithm: 'binary-tree',
                padding: 4
            }
        }),
        new webpack.ProvidePlugin({
            jQuery: "jquery",
            $: "jquery"
        })

    ]
}