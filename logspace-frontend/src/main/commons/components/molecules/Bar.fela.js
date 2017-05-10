// @flow

import {createComponent} from 'react-fela'
import {Flex} from 'indoqa-react-fela'

const Bar = ({theme}) => ({
  height: theme.layout.actionBarHeight,
  backgroundColor: theme.colors.bgLight,
  width: 'auto',
  alignItems: 'center',
  justifyContent: 'flex-start',
})

export default createComponent(Bar, Flex)
