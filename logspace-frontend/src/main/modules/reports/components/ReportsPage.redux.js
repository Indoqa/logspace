// @flow

import {connect} from 'react-redux'
import ReportsPage from './ReportsPage.react'
import {loadReports, toggleSort, setPage} from '../store/reports.actions'
import {selectPaging, selectResult, selectError, selectIsLoading} from '../store/reports.selectors'


const mapStateToProps = (state) => ({
  paging: selectPaging(state),
  result: selectResult(state),
  error: selectError(state),
  isLoading: selectIsLoading(state),
})

const mapDispatchToProps = (dispatch) => ({
  loadReports: () => {
    dispatch(loadReports())
  },

  toggleSort: (property: string) => {
    dispatch(toggleSort(property))
  },

  setPage: (page: number) => {
    dispatch(setPage(page))
  },
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ReportsPage)
