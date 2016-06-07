import {connect} from 'react-redux'
import Suggestions from './Suggestions.react'

import * as actions from '../../actions/suggestions'
import {addTimeSeries} from '../../actions/timeSeries'

const mapStateToProps = (state) => ({
  suggestions: state.suggestions
})

const mapDispatchToProps = (dispatch) => ({
  setSuggestionQuery: (query) => {
    dispatch(actions.setSuggestionQuery(query))
  },
  selectSystem: (system) => {
    dispatch(actions.selectSystem(system))
  },
  clearSystem: () => {
    dispatch(actions.clearSystem())
  },
  selectProperty: (property) => {
    dispatch(actions.selectProperty(property))
  },
  clearProperty: () => {
    dispatch(actions.clearProperty())
  },
  selectSpace: (space) => {
    dispatch(actions.selectSpace(space))
  },
  clearSpace: () => {
    dispatch(actions.clearSpace())
  },
  addTimeseries: (agentDescriptions) => {
    dispatch(addTimeSeries(agentDescriptions))
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Suggestions)
