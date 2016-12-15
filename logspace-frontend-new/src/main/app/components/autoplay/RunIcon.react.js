import React, {PropTypes} from 'react'
import {FontAwesome} from 'indoqa-rebass-components'
import 'font-awesome/css/font-awesome.css'

const getIconName = (running) => {
  if (running) {
    return 'pause'
  }

  return 'play'
}

export default class RefreshIcon extends React.Component {

  componentWillUnmount() {
    if (this.props.running) {
      this.props.onClick()
    }
  }

  render() {
    return (
      <FontAwesome icon={getIconName(this.props.running)} onClick={this.props.onClick} />
    )
  }
}

RefreshIcon.propTypes = {
  running: PropTypes.bool.isRequired,

  onClick: PropTypes.func,
}
