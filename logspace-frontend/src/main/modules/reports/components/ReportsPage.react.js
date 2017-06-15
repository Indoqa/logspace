// @flow

import React, {PropTypes} from 'react'
import {Box, Flex} from 'indoqa-react-fela'

import MainMenuPage from '../../../commons/components/templates/MainMenuPage.react'
import Action from '../../../commons/components/molecules/Action.react'
import DataTable from '../../../commons/components/molecules/DataTable.react'
import Paginator from '../../../commons/components/molecules/Paginator.react'

import UndoDeleteReport from './UndoDeleteReport.redux'

import type {Report} from '../types/Report'


export default class ReportsPage extends React.Component {

  static defaultProps = {
    result: null,
    error: null,
  }

  static propTypes = {
    paging: PropTypes.object.isRequired,
    result: PropTypes.object,
    isLoading: PropTypes.bool.isRequired,
    error: PropTypes.object,
    deletedReportId:PropTypes.string,

    loadReports: PropTypes.func.isRequired,
    toggleSort: PropTypes.func.isRequired,
    setPage: PropTypes.func.isRequired,
    copyReport: PropTypes.func.isRequired,
    deleteReport: PropTypes.func.isRequired,
  }

  createReportActions(report: Report) {
    return (
      <Flex>
        <Box p={1}>
          <Action onClick={() => this.props.copyReport(report)}>Copy</Action>
        </Box>
        <Box p={1}>
          <Action onClick={() => this.props.deleteReport(report.id)}>Delete</Action>
        </Box>
      </Flex>
    )
  }

  createReportData(report: Report) {
    return ReportsPage.createRow([report.id, report.name, report.branch, report.timestamp, this.createReportActions(report)])
  }

  static createRow(values: Array<any>) {
    return {
      cells: values.map((value) => ({content: value})),
    }
  }

  componentWillMount() {
    this.props.loadReports()
  }

  createSortableHeader(property: string, name: string) {
    return <Action onClick={() => this.props.toggleSort(property)}> {name} </Action>
  }

  createTableHeaderData() {
    return [
      {
        cells: [
          {content: this.createSortableHeader('id', 'ID')},
          {content: this.createSortableHeader('name', 'Name')},
          {content: this.createSortableHeader('branch', 'Branch')},
          {content: this.createSortableHeader('timestamp', 'Timestamp')},
          {content: ''},
        ],
      }
    ]
  }

  createTableBodyData() {
    const {result, isLoading} = this.props

    if (isLoading) {
      return [ReportsPage.createRow(['Loading...'])]
    }

    if (!result) {
      return [ReportsPage.createRow(['No result'])]
    }

    return result.reports.map((report) => this.createReportData(report))
  }

  createTableData() {
    return {
      widths: [80, 240, 80, 240],
      header: this.createTableHeaderData(),
      body: this.createTableBodyData(),
      footer: [],
    }
  }

  renderPaginator() {
    const totalCount = this.props.result ? this.props.result.totalCount : 0

    return <Paginator paging={this.props.paging} maxCount={totalCount} setPage={this.props.setPage} />
  }

  renderReports() {
    if (this.props.error) {
      return (<Box> Error loading reports ... </Box>)
    }

    return <DataTable data={this.createTableData()} />
  }

  render() {
    return (
      <MainMenuPage title="Reports" >
        <Box>
          <UndoDeleteReport />
          {this.renderReports()}
          {this.renderPaginator()}
        </Box>
      </MainMenuPage>
    )
  }
}
