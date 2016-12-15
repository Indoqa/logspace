/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import BasePage from '../../app/components/BasePage.react'

const BREADCRUMBS = [{children: 'Users'}]

export default class Page extends React.Component {
  render() {
    return (
      <BasePage title="Users" breadcrumbs={BREADCRUMBS}>
        USERS
      </BasePage>
    )
  }
}
