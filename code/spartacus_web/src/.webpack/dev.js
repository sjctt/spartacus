'use strict'

const webpack = require('webpack')
const merge = require('webpack-merge')
const baseConfig = require('./base')

const MiniCssExtractPlugin  = require('mini-css-extract-plugin')


const HOST = 'localhost'
const PORT = 80

module.exports = merge(baseConfig, {
  mode: 'development',

  devServer: {
    clientLogLevel: 'warning',
    hot: true,
    contentBase: 'dist',
    compress: true,
    host: HOST,
    port: PORT,
    open: true,
    overlay: { warnings: false, errors: true },
    publicPath: '/',
    quiet: true,
    watchOptions: {
      poll: false,
      ignored: /node_modules/
    },
    //contentBase:'./',
    // proxy:{
    //     // 当你请求是以/api开头的时候，则我帮你代理访问到http://localhost:9090
    //     // 例如：
    //     // /api/users  http://localhost:3000/api/users
    //     // 我们真是服务器接口是没有/api的 
    //     "/api":{
    //         target:"http://127.0.0.1:1991",
    //         pathRewrite:{"^/api":"/static/json"},
    //         changeOrigin:true
    //     }
    // }
  },

  module: {
   rules: [
      {
        test: /\.css$/,
        use: [MiniCssExtractPlugin.loader,"css-loader"]
    },
    // Scss compiler
    {
        test: /\.scss$/,
        use: [
            MiniCssExtractPlugin.loader,
            "css-loader",
            "sass-loader?indentedSyntax"
        ]
    }
    ]
  },

  plugins: [
    new webpack.HotModuleReplacementPlugin(),
    new MiniCssExtractPlugin({
      filename: "assets/css/[name].[hash:4].css"
  })
  ]
 
})
