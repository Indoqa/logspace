/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
var gulp = require('gulp');
var bg = require('gulp-bg');
var replace = require('gulp-replace');
var minifyHTML = require('gulp-minify-html');

gulp.task('dist', ['build'], function(done) {
  gulp.src('site/index.html')
    .pipe(replace(
      /\/\/localhost:8000\/build\/app.js/gim,
      'app.js'
    ))
    .pipe(replace(
      /<!--\s*build:css(?:\(([^\)]+?)\))?\s+(\/?([^\s]+?))?\s*-->[\s\S]*<!--\s*endbuild\s*-->/gim,
      '<link rel="stylesheet" type="text/css" href="app.css" />'))
    .pipe(minifyHTML())
    .pipe(gulp.dest('build/'));

  return
});

gulp.task("dist-run", bg('node', '--harmony', 'src/server/dist.js', 'dist'));
