// @flow

import {createComponent} from 'react-fela'

import Action from './Action.react'

const PaginatorItemRule = ({theme, visible, current, enabled}) => {
  const result = {}

  if (!visible) {
    result.display = 'none'
  }

  if (!enabled) {
    result.cursor = 'auto'
    result.color = theme.colors.disabled
  }

  if (current) {
    result.fontWeight = 'bold'
    result.color = theme.colors.text
  }

  return result
}

export default createComponent(PaginatorItemRule, Action)
