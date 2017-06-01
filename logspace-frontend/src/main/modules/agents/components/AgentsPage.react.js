// @flow

import React, {PropTypes} from 'react'
import {Box, Flex} from 'indoqa-react-fela'

import AutoReload from '../../../commons/components/organisms/AutoReload.react'
import Dropdown from '../../../commons/components/molecules/Dropdown.react'
import MainMenuPage from '../../../commons/components/templates/MainMenuPage.react'
import Paginator from '../../../commons/components/molecules/Paginator.react'

import type {AgentActivity} from '../types/AgentActivity'
import type {DropdownOption} from '../../../commons/types/DropdownOption'


export default class AgentsPage extends React.Component {

  static defaultProps = {
    result: null,
    error: null,
  }

  static propTypes = {
    autoReload: PropTypes.object.isRequired,
    interval: PropTypes.object.isRequired,
    sorting: PropTypes.object.isRequired,
    paging: PropTypes.object.isRequired,
    result: PropTypes.object,
    error: PropTypes.object,

    loadActivities: PropTypes.func.isRequired,
    setInterval: PropTypes.func.isRequired,
    setSorting: PropTypes.func.isRequired,
    setPage: PropTypes.func.isRequired,
    startAutoReload: PropTypes.func.isRequired,
    stopAutoReload: PropTypes.func.isRequired,
  }

  static renderActivity(activity: AgentActivity) {
    return (
      <Flex direction="column">
        <Box pt={2}>{activity.globalAgentId}</Box>
        <Flex>
          {activity.history.map((value) => <Box p={1}>{value}</Box>)}
        </Flex>
      </Flex>
    )
  }

  componentWillMount() {
    this.props.loadActivities()
  }

  componentWillUnmount() {
    this.props.stopAutoReload()
  }

  createHeader() {
    const sortOptions: Array<DropdownOption> = [
      {name: 'Name', value: {property: 'index', ascending: true}},
      {name: 'Activity', value: {property: 'count', ascending: false}},
    ]

    const intervalOptions: Array<DropdownOption> = [
      {name: '5 min', value: 300},
      {name: '15 min', value: 900},
      {name: '30 min', value: 1800},
      {name: '1 h', value: 3600},
      {name: '2 h', value: 7200},
      {name: '6 h', value: 14400},
      {name: '12 h', value: 43200},
      {name: '24 h', value: 86400},
    ]

    return (
      <Flex>
        <Box mr={1}>
          <Dropdown options={sortOptions} selectedValue={this.props.sorting} onChange={this.props.setSorting} />
        </Box>
        <Box mr={1}>
          <Dropdown options={intervalOptions} selectedValue={this.props.interval.seconds} onChange={this.props.setInterval} />
        </Box>
        <AutoReload
          autoReload={this.props.autoReload}
          reload={this.props.loadActivities}
          start={this.props.startAutoReload}
          stop={this.props.stopAutoReload}
        />
      </Flex>
    )
  }

  renderActivities() {
    const {error, autoReload, result} = this.props

    if (error) {
      return (<Box> Error loading activities ... </Box>)
    }

    if (autoReload.isLoading) {
      return (<Box> Loading ... </Box>)
    }

    if (!result || !result.agentActivities || result.totalCount === 0) {
      return (<Box> No result ... </Box>)
    }

    return result.agentActivities.map(AgentsPage.renderActivity)
  }

  renderPaginator() {
    const totalCount = this.props.result ? this.props.result.totalCount : 0

    return <Paginator paging={this.props.paging} maxCount={totalCount} setPage={this.props.setPage} />
  }

  render() {
    return (
      <MainMenuPage title="Agents" header={this.createHeader()} >
        <Box>
          {this.renderActivities()}
          {this.renderPaginator()}
        </Box>
      </MainMenuPage>
    )
  }
}
