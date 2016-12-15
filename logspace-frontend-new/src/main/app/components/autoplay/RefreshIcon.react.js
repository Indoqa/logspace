import React, {PropTypes} from 'react'
import {FontAwesome, Span} from 'indoqa-rebass-components'
import 'font-awesome/css/font-awesome.css'


export default class RefreshIcon extends React.Component {
  getFormattedCountdown() {
    if (this.props.countdown === 0) {
      return ''
    }

    return this.props.countdown
  }

  performClick() {
    if (!this.props.onClick || this.props.loading) {
      return
    }

    this.props.onClick()
  }

  render() {
    return (
      <Span style={{position: 'relative'}}>
        <FontAwesome icon="repeat" className={this.props.loading ? 'RefreshLoading' : ''} onClick={() => this.performClick()} />
        <Span style={{fontSize: '8px', position: 'absolute', top: '5px', left: '0px', width: '100%', textAlign: 'center'}}>
          {this.getFormattedCountdown()}
        </Span>
      </Span>
    )
  }
}

RefreshIcon.propTypes = {
  countdown: PropTypes.number.isRequired,
  loading: PropTypes.bool,
  onClick: PropTypes.func,
}
