/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
/* @flow weak */

'use strict';

var webpack = require('webpack');
var gutil = require('gulp-util');

module.exports = function(webpackConfig) {
  return function(callback) {
    webpack(webpackConfig, function(fatalError, stats) {
      var jsonStats = stats.toJson();
      var buildError = fatalError || jsonStats.errors[0] || jsonStats.warnings[0];

      if (buildError)
        throw new gutil.PluginError('webpack', buildError);

      gutil.log('[webpack]', stats.toString({
        colors: true,
        version: false,
        hash: false,
        timings: false,
        chunks: false,
        chunkModules: false
      }));

      callback();
    });
  };
};
