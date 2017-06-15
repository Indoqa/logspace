// @flow

import {connect} from 'react-redux'
import UndoDeleteReport from './UndoDeleteReport.react'
import {setDeletedReportId, restoreDeletedReport} from '../store/reports.actions'
import {selectDeletedReportId} from '../store/reports.selectors'


const mapStateToProps = (state) => ({
  deletedReportId: selectDeletedReportId(state),
})

const mapDispatchToProps = (dispatch) => ({
  setDeletedReportId: (id: string) => {
    dispatch(setDeletedReportId(id))
  },

  restoreDeletedReport: () => {
    dispatch(restoreDeletedReport())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UndoDeleteReport)
