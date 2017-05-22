// @flow

import React, {PropTypes} from 'react'
import {Box} from 'indoqa-react-fela'
import MainMenuPage from '../../../commons/components/templates/MainMenuPage.react'

import type {Report} from '../types/Report'

class ReportsPage extends React.Component {

  static renderReport(report: Report) {
    return (<Box key={report.id}> {report.id} {report.name} </Box>)
  }

  componentWillMount() {
    this.props.loadReports()
  }

  renderReports() {
    const {isLoading, result} = this.props

    if (isLoading) {
      return (<Box> Loading ... </Box>)
    }

    if (!result) {
      return <Box> No result ... </Box>
    }

    return result.reports.map(ReportsPage.renderReport)
  }

  render() {
    return (
      <MainMenuPage title="Reports" >
        <Box>
          {this.renderReports()}
        </Box>
      </MainMenuPage>
    )
  }
}

ReportsPage.propTypes = {
  result: PropTypes.object,
  isLoading: PropTypes.bool.isRequired,
  error: PropTypes.object,

  loadReports: PropTypes.func.isRequired,
}

ReportsPage.defaultProps = {
  result: null,
  error: null,
}

export default ReportsPage
