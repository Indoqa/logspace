# Actions and action creators

Assuming you are familiar with the basic concepts of [actions and action creators](http://redux.js.org/docs/basics/Actions.html) and how [middlewares](http://redux.js.org/docs/advanced/Middleware.html) are working, we express some guidlines of how we write actions and use a composition of multiple middlewares. 

## Best practices

  * Always use constants for the `type` property. See [why](http://redux.js.org/docs/recipes/ReducingBoilerplate.html).
  * Use a `payload` property to store all information relevant for the reducer. This makes reducers look familiar as they always consume `action.payload`.
  * Use consistent naming for the action creator method and according action type. Convention is `VERB_PHRASE` respectively `verbPhrase()`.
  * Write action creator functions using the simplest possible es6 syntax.
  * Always use promises for asynchronous stuff (redux-promise-middleware).
  * Wrap the created action into a function to access the redux `store` object (custom inject middleware).
  * Return arrays to execute multiple actions in one creator (redux-multi).
  * Don't use redux-thunk as it's just another way of doing asynchronous or multi actions. 

## Details

Using our middleware setup, we add support for asynchronous actions, store access and multiple action dispatching. The suitable middlewares are activated depending on the return type of the action creator function:

### Plain objects as described in vanilla redux

```javascript
export const ADD_TODO = 'ADD_TODO'

// action creator for ADD_TODO
export function addTodo(text) {
  return {
    type: ADD_TODO,
    payload: {text: text}
  }
}

// which can be written as arrow function
export const addTodo = (text) => {
  return {
    type: ADD_TODO,
    payload: {text}
  }
}

// and even shorter, as the function directly returns (= the preferred way)
export const addTodo = (text) => ({
  type: ADD_TODO,
  payload: {text}
})
```
### Using a promise as `payload` for asynchronous actions

If you return a promise as payload, the [react-promise-middleware](https://github.com/pburtchaell/redux-promise-middleware) kicks in and resolves this promise before dispatching the result. The main usecase for this is to execute a fetch. 
```javascript
export const FETCH_DATA = 'FETCH_DATA'

export function fetchData(count) {
  return {
    type: FETCH_DATA,
    payload: fetch(`/api/v1/data?count=${count}`).then(result => result.json())
  }
}

// in es6 (= the preferred way)
export const fetchData = (count) => ({
  type: FETCH_DATA,
  payload: fetch(`/api/v1/data?count=${count}`).then(result => result.json())
})
```

The middleware then dispatches two actions:
  * `FETCH_DATA_START` with an empty `payload` before the promise is executed. A reducer may react on that set a 'loading' state.
  * `FETCH_DATA_SUCCESS` after a successful execution. The `payload` then represents the result OR `FETCH_DATA_ERROR` if an error occurs during promise execution. The `payload` then represents the error object.
 
Note: We configured the suffixes to _START, _SUCCESS and _ERROR in /src/main/store.js. 
  
  
### Accessing the current state in action creators

Sometimes it's necessary to have read-only access to the current reducer state before returning an action. Given the example above, we can change fetchData() to look up the `count` property in the state instead of getting it as an argument. Using our inject middleware, the root redux `store` object may be accessed by wrapping the action result into a function that gets the dependecies as argument: 

```javascript
export const FETCH_DATA = 'FETCH_DATA'

export function fetchData() {
  return function({store}) {
    const count = store.getState().get('query').get('count')
    return {
      type: FETCH_DATA,
      payload: fetch(`/api/v1/data?count=${count}`).then(result => result.json())
    }
  }
}

// es6 step 1: returning an arrow function
export function fetchData() {
  return ({store}) => {
    const count = store.getState().get('query').get('count')
    return {
      type: FETCH_DATA,
      payload: fetch(`/api/v1/data?count=${count}`).then(result => result.json())
    }
  }
}

// es6 step 2: write action creator as arrow function as well
export const fetchData = () => {
  return ({store}) => ({
    const count = store.getState().get('query').get('count')
    return {
      type: FETCH_DATA,
      payload: fetch(`/api/v1/data?count=${count}`).then(result => result.json())
    }
  })
}

// es6 step 3: simplify the chaining of both functions using currying (= the preferred way)
export const fetchData = () => ({store}) => {
  const count = store.getState().get('query').get('count')
  return {
    type: FETCH_DATA,
    payload: fetch(`/api/v1/data?count=${count}`).then(result => result.json())
  }
}
```

### Dispatching multiple actions with one creator

If you need to dispatch multiple actions in one creator, just return an array of actions. The [react-multi middleware](https://github.com/ashaffer/redux-multi) then dispatches them sequentially. 

```javascript
export const DO_ACTION1 = 'DO_ACTION1'
export const DO_ACTION2 = 'DO_ACTION2'
export const DO_BOTH = 'DO_BOTH'

export const doAction1 = (data1) => ({
 type: DO_ACTION1,
 payload: data1
})

export const doAction2 = (data2) => ({
 type: DO_ACTION2,
 payload: data2
})

export const doBoth = (data1, data2) => [
   doAction1(data1),
   doAction2(data2)
]
```


