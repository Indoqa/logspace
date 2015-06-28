/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import Router from 'react-router'
import routes from './routes'

const app = document.getElementById('app')

Router.run(routes, Router.HashLocation, (Handler) => {
  React.render(<Handler />, app)
})
