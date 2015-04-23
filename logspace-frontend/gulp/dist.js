var gulp = require('gulp');
var bg = require('gulp-bg');
var replace = require('gulp-replace');
var minifyHTML = require('gulp-minify-html');

gulp.task('dist', ['build'], function(done) {
  gulp.src('site/index.html')
    .pipe(replace(
      /\/\/localhost:8888\/build\/app.js/gim,
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
