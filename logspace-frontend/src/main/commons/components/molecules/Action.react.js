// @flow

import {createComponent} from 'react-fela'

const Action = () => ({
  textTransform: 'uppercase',
  cursor: 'pointer',
})

export default createComponent(Action, 'a', ['onClick'])
