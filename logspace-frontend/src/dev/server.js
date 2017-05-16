const url = require('url')
const options = require('./../../indoqa-webpack-options.js')
const runDevServer = require('indoqa-webpack').runDevServer

const routesCallback = ({app, proxy}) => {
  app.use('/reports', proxy('http://localhost:3456/reports', {
    forwardPath: (req) => {
      return `${url.parse(req.url).path}`
    },
    decorateRequest: (req) => {
      // add Authorization if needed
      // req.headers['Authorization'] = 'Basic 12345='
      return req
    }
  }))
}

runDevServer({options, routesCallback})
