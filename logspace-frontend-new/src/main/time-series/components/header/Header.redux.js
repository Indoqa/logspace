/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {connect} from 'react-redux'
import Header from '../../../app/components/header/Header.react'

import {showOptions} from '../../actions/options'

const mapStateToProps = () => ({
  //
})

const mapDispatchToProps = (dispatch) => ({
  showOptions: () => {
    dispatch(showOptions())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Header)
