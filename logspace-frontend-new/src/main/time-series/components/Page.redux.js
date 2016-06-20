/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {connect} from 'react-redux'
import Page from './Page.react'

import {refreshResult} from '../actions/result'
import {refreshSuggestions} from '../actions/suggestions'

const mapStateToProps = (state) => ({
  navDrawerCss: state.drawer.get('navDrawerCss'),
  mainCss: state.drawer.get('mainCss')
})

const mapDispatchToProps = (dispatch) => ({
  initialize: () => {
    console.log('TODO: implement initialize()')
    dispatch(refreshResult())
    dispatch(refreshSuggestions())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Page)
