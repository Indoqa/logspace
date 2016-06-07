/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'
import Tabs from 'react-simpletabs'

import TimeWindowCustom from './TimeWindowCustom.react'
import TimeWindowDynamic from './TimeWindowDynamic.react'
import TimeWindowShortcuts from './TimeWindowShortcuts.react'

import './TimeWindow.styl'

export default class TimeWindow extends React.Component {
  render() {
    return (
      <div>
        <Tabs tabActive={this.props.activeTab} onAfterChange={this.props.openTab}>
          <Tabs.Panel title="Shortcuts">
            <TimeWindowShortcuts selectPredefinedDate={this.props.selectPredefinedDate} />
          </Tabs.Panel>
          <Tabs.Panel title="Dynamic">
             <TimeWindowDynamic selectDynamicDate={this.props.selectDynamicDate} dynamic={this.props.dynamic} />
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
  dynamic: PropTypes.object.isRequired,
  selectDynamicDate: PropTypes.func.isRequired,
  selectPredefinedDate: PropTypes.func.isRequired
}
