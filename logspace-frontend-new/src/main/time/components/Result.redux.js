import {connect} from 'react-redux'
import Result from './Result.react'

const mapStateToProps = (state) => ({
  result: state.time.get('result'),
  error: state.time.get('error'),
  isLoading: state.time.get('isLoading'),
})

const mapDispatchToProps = () => ({
  //
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Result)
