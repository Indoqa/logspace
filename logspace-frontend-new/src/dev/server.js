const webpack = require('webpack')
const webpackDevMiddleware = require('webpack-dev-middleware')
const webpackHotMiddleware = require('webpack-hot-middleware')
const path = require('path')

const app = new (require('express'))()
const proxy = require('express-http-proxy')
const url = require('url')

const config = require('./../../webpack.config')
const compiler = webpack(config)

app.use(webpackDevMiddleware(compiler, {
  headers: {'Access-Control-Allow-Origin': '*'},
  noInfo: true,
  publicPath: config.output.publicPath
}))

app.use(webpackHotMiddleware(compiler))

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


app.get('*', (req, res) => {
  res.sendFile(path.resolve(`${__dirname}./../main/index.html`))
})

app.listen(config.hotPort, () => {
  console.log('==> Hot server started at port %d', config.hotPort) // eslint-disable-line no-console
})

app.listen(config.devPort, (error) => {
  if (error) {
    console.error(error)  // eslint-disable-line no-console
  } else {
    console.info( // eslint-disable-line no-console
      '==> Listening on port %s. Open up http://localhost:%s/ in your browser.',
      config.devPort,
      config.devPort
    )
  }
})
