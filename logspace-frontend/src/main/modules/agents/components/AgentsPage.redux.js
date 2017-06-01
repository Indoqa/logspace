// @flow

import {connect} from 'react-redux'
import AgentsPage from './AgentsPage.react'
import {loadActivities, setInterval, setSorting, setPage, startAutoReload, stopAutoReload} from '../store/agents.actions'
import {selectAutoReload, selectInterval, selectPaging, selectSorting, selectResult, selectError} from '../store/agents.selectors'

import type {Sorting} from '../../../commons/types/Sorting'


const mapStateToProps = (state) => ({
  autoReload: selectAutoReload(state),
  interval: selectInterval(state),
  sorting: selectSorting(state),
  paging: selectPaging(state),
  result: selectResult(state),
  error: selectError(state),
})

const mapDispatchToProps = (dispatch) => ({
  loadActivities: () => {
    dispatch(loadActivities())
  },

  setSorting: (sorting: Sorting) => {
    dispatch(setSorting(sorting))
  },

  setPage: (page: number) => {
    dispatch(setPage(page))
  },

  setInterval: (seconds: number) => {
    dispatch(setInterval(seconds))
  },

  startAutoReload: () => {
    dispatch(startAutoReload())
  },

  stopAutoReload: () => {
    dispatch(stopAutoReload())
  },
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AgentsPage)
