/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'
import Tabs from 'react-simpletabs'

import TimeWindowCustom from './time-window-custom.react'
import TimeWindowDynamic from './time-window-dynamic.react'
import TimeWindowShortcuts from './time-window-shortcuts.react'

import './time-window.styl'

export default class TimeWindow extends React.Component {
  render() {
    return (
      <div>
        <Tabs tabActive={this.props.activeTab} onAfterChange={this.props.openTab}>
          <Tabs.Panel title="Shortcuts">
            <TimeWindowShortcuts {...this.props} />
          </Tabs.Panel>
          <Tabs.Panel title="Dynamic">
             <TimeWindowDynamic {...this.props} />
          </Tabs.Panel>
          <Tabs.Panel title="Custom">
             <TimeWindowCustom {...this.props} />
          </Tabs.Panel>
        </Tabs>
      </div>
    )
  }
}

TimeWindow.propTypes = {
  activeTab: PropTypes.string.isRequired,
  openTab: PropTypes.func.isRequired,
}
