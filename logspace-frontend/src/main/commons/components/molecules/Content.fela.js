// @flow

import {createComponent} from 'react-fela'
import {Box} from 'indoqa-react-fela'

const Content = ({theme}) => ({
  height: `calc(100% - ${theme.layout.actionBarHeight}px)`,
})

export default createComponent(Content, Box)
