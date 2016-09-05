/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
const url = require('url')
const options = require('./../../indoqa-webpack-options.js')
const runDevServer = require('indoqa-webpack').runDevServer

const routesCallback = ({app, proxy}) => {
  app.use('/logspace', proxy('http://localhost:4567', {
    forwardPath: (req) => {
      // modify url path if needed
      return url.parse(req.url).path
    },
    decorateRequest: (req) => {
      // add Authorization if needed
      // req.headers['Authorization'] = 'Basic 12345='
      return req
    }
  }))
}

runDevServer({options, routesCallback})
