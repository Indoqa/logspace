// @flow

import {createComponent} from 'react-fela'

const Logo = ({theme}) => ({
  height: '50px',
  cursor: 'pointer',
  display: 'inline-flex',
  alignItems: 'center',
  padding: theme.spacing.space1,
})

export default createComponent(Logo, 'div', ['onClick'])
