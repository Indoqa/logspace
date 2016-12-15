/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import {Link} from 'react-router'
import {ROUTE_AGENT_ACTIVITY, ROUTE_REPORTS} from '../../../routes'

import './Navigation.styl'

export default class Page extends React.Component {
  render() {
    return (
      <div className="navigation">
        <ul className="navigation-items">
          <li className="navigation-item"><Link to={ROUTE_AGENT_ACTIVITY}> Agents </Link></li>
          <li className="navigation-item"><Link> Dashboards </Link></li>
          <li className="navigation-item"><Link to={ROUTE_REPORTS}> Reports </Link></li>
          <li className="navigation-item"><Link> Alerts </Link></li>
          <li className="navigation-item"><Link> Users </Link></li>
        </ul>
      </div>
    )
  }
}
