// @flow

import {createComponent} from 'react-fela'

const RightDrawer = ({active, theme}) => ({
  width: 300,
  marginRight: (active) ? 0 : -300,
  transition: 'margin 200ms',
  backgroundColor: theme.colors.bgLight,
})

export default createComponent(RightDrawer)
