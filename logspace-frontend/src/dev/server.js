const url = require('url')
const options = require('./../../indoqa-webpack-options.js')
const runDevServer = require('indoqa-webpack').runDevServer

const routesCallback = ({app, proxy}) => {
  app.use('/api', proxy('http://localhost:4567', {
    forwardPath: (req) => {
      console.log(req.url)
      const result =  `/api${url.parse(req.url).path}`
      console.log(req.url + ' -> ' + result)
      return result
    },
    decorateRequest: (req) => {
      // add Authorization if needed
      // req.headers['Authorization'] = 'Basic 12345='
      return req
    }
  }))
}

runDevServer({options, routesCallback})
