/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
const DEV_PORT = 3000
const HOT_RELOAD_PORT = 3001

const path = require('path')
const webpack = require('webpack')
const ExtractTextPlugin = require('extract-text-webpack-plugin')

module.exports = {
  devPort: DEV_PORT,
  hotPort: HOT_RELOAD_PORT,
  devtool: 'cheap-module-eval-source-map',
  entry: [
    `webpack-hot-middleware/client?path=http://localhost:${HOT_RELOAD_PORT}/__webpack_hmr`,
    path.join(__dirname, './src/main/index.js')
  ],
  output: {
    path: path.join(__dirname, 'target/app'),
    filename: 'app.js',
    publicPath: ''
  },
  plugins: [
    new webpack.optimize.OccurenceOrderPlugin(),
    new webpack.HotModuleReplacementPlugin(),
    new ExtractTextPlugin('app.css')
  ],
  module: {
    loaders: [
      {
        test: /\.js$/,
        loaders: ['babel'],
        exclude: /node_modules/,
        include: path.join(__dirname, './src/main')
      },
      {
        test: /\.styl$/,
        loader: 'style-loader!css-loader!stylus-loader?paths=node_modules/bootstrap-stylus/stylus/',
        exclude: /node_modules/,
        include: path.join(__dirname, './src/main')
      },
      {
        test: /\.css$/,
        loader: ExtractTextPlugin.extract('style-loader', 'css-loader')
      },
      {
        test: /\.woff(\?v=\d+\.\d+\.\d+)?$/,
        loader: 'url?limit=10000&mimetype=application/font-woff'
      }, {
        test: /\.woff2(\?v=\d+\.\d+\.\d+)?$/,
        loader: 'url?limit=10000&mimetype=application/font-woff'
      }, {
        test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/,
        loader: 'url?limit=10000&mimetype=application/octet-stream'
      }, {
        test: /\.eot(\?v=\d+\.\d+\.\d+)?$/,
//        loader: 'file'
        loader: 'url?limit=10000&mimetype=application/octet-stream'
      }, {
        test: /\.svg(\?v=\d+\.\d+\.\d+)?$/,
        loader: 'url?limit=10000&mimetype=image/svg+xml'
      }, {
        test: /\.png(\?v=\d+\.\d+\.\d+)?$/,
        loader: 'url?limit=10000&mimetype=image/png'
      }
    ]
  }
}
