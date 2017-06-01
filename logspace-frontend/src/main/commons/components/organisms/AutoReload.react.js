// @flow

import React, {PropTypes} from 'react'
import {Box, Flex} from 'indoqa-react-fela'

import Action from '../molecules/Action.react'


export default class AutoReload extends React.Component {

  static propTypes = {
    autoReload: PropTypes.object.isRequired,

    reload: PropTypes.func.isRequired,
    start: PropTypes.func.isRequired,
    stop: PropTypes.func.isRequired,
  }

  getToggleActionText() {
    const {autoReload} = this.props

    if (autoReload.active && !autoReload.isLoading) {
      return `Automatic Reload in ${autoReload.countdown}`
    }

    if (autoReload.isLoading) {
      return 'Loading ...'
    }

    return 'Enable Automatic Reload'
  }

  render() {
    const toggleFunction = (this.props.autoReload.active ? this.props.stop : this.props.start)

    return (
      <Flex>
        <Box pr={1}><Action onClick={toggleFunction}>{this.getToggleActionText()}</Action></Box>
        <Box pr={1}><Action onClick={this.props.reload}>Reload Now</Action></Box>
      </Flex>
    )
  }
}
