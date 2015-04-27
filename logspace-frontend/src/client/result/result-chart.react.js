/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react';

export default class Chart extends React.Component {
  render() {
    return (
      <div>
        showing a chart for Timewindow: <b>{this.props.timeWindow.get("start")}</b> to <b>{this.props.timeWindow.get("end")}</b>
        <br/>
        <br/>
        DATA: {this.props.result.get("data")}
      </div>
    );
  }
}
