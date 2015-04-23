/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
/* @flow weak */

'use strict';

var notifier = require('node-notifier');
var path = require('path');

function getLocMessage(error, loc) {
  var filePath = error.module.resource.split(path.sep);
  return [
    filePath[filePath.length - 1],
    ' at [',
    loc.line,
    ',',
    loc.column,
    ']'
  ].join('');
}

module.exports = function() {
  this.plugin('done', function(stats) {
    // TODO: Handle warnings as well.
    var error = stats.compilation.errors[0];
    if (!error) return;
    var loc = error.error.loc;
    var msg;
    if (loc)
      msg = getLocMessage(error, loc);
    else if (error.message)
      msg = error.message;
    else
      return;

    notifier.notify({
      title: 'Webpack Error',
      message: msg
    });
  });
};
