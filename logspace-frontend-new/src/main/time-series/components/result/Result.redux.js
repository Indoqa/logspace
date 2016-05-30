import {connect} from 'react-redux'
import Result from './result.react'

const mapStateToProps = (state) => ({
  autoPlay: state.result.get('autoPlay'),
  autoPlaySchedule: state.result.get('autoPlaySchedule'),
  chartType: state.result.get('chartType'),
  chartTitle: state.result.get('chartTitle'),
  chartTitleEditable: state.editable.get('result'),
  result: state.result.get('translatedResult'),
  timeSeries: state.timeSeries.get('timeSeries')
})

const mapDispatchToProps = () => ({
  //
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Result)
