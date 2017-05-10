const url = require('url')
const options = require('./../../indoqa-webpack-options.js')
const runDevServer = require('indoqa-webpack').runDevServer

const routesCallback = ({app, proxy}) => {
  app.use('/geonames', proxy('http://api.geonames.org', {
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
