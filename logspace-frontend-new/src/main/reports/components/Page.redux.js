/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {connect} from 'react-redux'
import Page from './Page.react'

import * as reportsActions from '../actions/reports'

const mapStateToProps = (state) => ({
  loading: state.reports.get('loading'),
  reports: state.reports.get('reports'),
})

const mapDispatchToProps = (dispatch) => ({
  loadReports: () => {
    dispatch(reportsActions.loadReports())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Page)
