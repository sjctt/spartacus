"use strict";
const path = require('path');
const fs = require('fs');

const appDirectory = fs.realpathSync(process.cwd());
const resolveApp = relativePath => path.resolve(appDirectory, relativePath);

const CleanWebpackPlugin = require('clean-webpack-plugin');
const HtmlWebPackPlugin = require("html-webpack-plugin");
const CopyWebpackPlugin = require("copy-webpack-plugin");
const { VueLoaderPlugin } = require("vue-loader");

const utils = require("./utils");


let pathsToClean = [
  'build'
]

// the clean options to use
let cleanOptions = {
  root: __dirname,
  verbose: false, // Write logs to console.
  dry: false
}

module.exports = {
  output: {
    path: resolveApp('dist'),
    filename: 'assets/js/[name].[hash:4].js',
    chunkFilename: 'assets/js/[name].[hash:4].chunk.js',
    publicPath: '/',
},

  resolve: {
    extensions: [".js", ".vue", ".json"],
    alias: {
      assets: utils.resolve("../src/assets"),
      pages: utils.resolve("../src/pages"),
      components: utils.resolve("../src/components"),
      containers: utils.resolve("../src/containers"),
      constants: utils.resolve("../src/constants"),   
      data: utils.resolve("../src/data"),   
      utils: utils.resolve("../src/utils"),
      views: utils.resolve("../src/views"),
      static:utils.resolve("../static")
    }
  },
  module: {
    rules: [
      {
        test: /\.vue$/,
        use: "vue-loader"
      },
      {
        test: /\.m?js$/,
        exclude: /(node_modules|bower_components)/,
        use: {
          loader: "babel-loader",
          options: {
            presets: ["@babel/preset-env"]
          }
        }
      }
    ]
  },

  plugins: [
 
    new CleanWebpackPlugin(pathsToClean, cleanOptions),
    new HtmlWebPackPlugin({
      template: "./index.html",
      filename: "./index.html",
      favicon: "./favicon.ico"
    }),
    new VueLoaderPlugin(),
    new CopyWebpackPlugin([
      { from: "src/assets/img", to: "assets/img" },
      { from: "src/assets/fonts", to: "assets/fonts" },
      { from: "src/views/", to: "views" },
      { from: "static/", to: "static" }
    ])
   
  
  ]
};
