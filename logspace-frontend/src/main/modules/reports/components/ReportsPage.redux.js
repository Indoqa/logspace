// @flow

import {connect} from 'react-redux'
import ReportsPage from './ReportsPage.react'
import {loadReports} from '../store/reports.actions'
import {selectResult, selectError, selectIsLoading} from '../store/reports.selectors'


const mapStateToProps = (state) => ({
  result: selectResult(state),
  error: selectError(state),
  isLoading: selectIsLoading(state),
})

const mapDispatchToProps = (dispatch) => ({
  loadReports: () => {
    dispatch(loadReports())
  },
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ReportsPage)
