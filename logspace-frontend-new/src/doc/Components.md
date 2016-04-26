tbd.

### Components vs. Containers

tbd.

### Component classes vs. functional components

tbd.

Important note: Top level route components NEED to be react classes and can't be written as functional components! Otherwise hot reloading won't work: https://github.com/gaearon/react-hot-loader/issues/212. So in the follwing example, App, TodoPage and TimePage need to be classes:

```javascript
import React from 'react'
import {IndexRoute, Route} from 'react-router'
import App from './app/components/App.react'
import TimePage from './time/components/Page.react'
import TodoPage from './todos/components/Page.react'

const routes = (
  <Route component={App} path="/">
    <IndexRoute component={TimePage} />
    <Route component={TodoPage} path="/todos" />
  </Route>
)
```
