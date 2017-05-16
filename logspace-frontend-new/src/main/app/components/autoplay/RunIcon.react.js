/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
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
