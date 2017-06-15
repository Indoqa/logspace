// @flow

import React, {PropTypes} from 'react'
import {Box, Flex} from 'indoqa-react-fela'

import Action from '../../../commons/components/molecules/Action.react'


export default class UndoDeleteReport extends React.Component {

  static defaultProps = {
    deletedReportId: null,
  }

  static propTypes = {
    deletedReportId:PropTypes.string,

    setDeletedReportId: PropTypes.func.isRequired,
    restoreDeletedReport: PropTypes.func.isRequired,
  }

  render() {
    if (this.props.deletedReportId) {
      return (
        <Flex p={1}>
          <Box grow={1}>
            <Action onClick={() => this.props.restoreDeletedReport()}> Undo delete report </Action>
          </Box>
          <Box>
            <Action onClick={() => this.props.setDeletedReportId(null)}> x </Action>
          </Box>
        </Flex>
      )
    }

    return null
  }
}
