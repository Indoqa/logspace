/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'
import DrawerPanel from './DrawerPanel.react'

require('./Drawer.styl')

export default class Drawer extends React.Component {

  render() {
    const {close, activePanel, msg} = this.props
    const closeIcon = '<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1" id="Layer_1" x="0px" y="0px" width="20px" height="20px" viewBox="0 0 512 512" style="enable-background:new 0 0 512 512;" xml:space="preserve"><path d="M437.5,386.6L306.9,256l130.6-130.6c14.1-14.1,14.1-36.8,0-50.9c-14.1-14.1-36.8-14.1-50.9,0L256,205.1L125.4,74.5  c-14.1-14.1-36.8-14.1-50.9,0c-14.1,14.1-14.1,36.8,0,50.9L205.1,256L74.5,386.6c-14.1,14.1-14.1,36.8,0,50.9  c14.1,14.1,36.8,14.1,50.9,0L256,306.9l130.6,130.6c14.1,14.1,36.8,14.1,50.9,0C451.5,423.4,451.5,400.6,437.5,386.6z" fill="#FFFFFF"/></svg>' // eslint-disable-line max-len

    return (
      <div className="drawer">
        <div className="header">
          <div className="close">
            <span
              onClick={() => close()}
              dangerouslySetInnerHTML={{__html: closeIcon}}
            />
          </div>
          <div className="title"> {msg(`drawer.header.${activePanel}`)} </div>
        </div>
        <div className="panel">
          <DrawerPanel activePanel={activePanel} />
        </div>
      </div>
    )
  }
}

Drawer.propTypes = {
  msg: PropTypes.func.isRequired,
  activePanel: PropTypes.object,
  close: PropTypes.func.isRequired
}
