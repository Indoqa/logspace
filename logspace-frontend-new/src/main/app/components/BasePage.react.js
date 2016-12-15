/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'
import {Box} from 'reflexbox'
import Header from './header/Header.react'

export default class BasePage extends React.Component {
  render() {
    return (
      <Box>
        <Header title={this.props.title} breadcrumbs={this.props.breadcrumbs || []} >
          {this.props.headerComponents}
        </Header>
        <Box className="Content" p={0}>
          {this.props.children}
        </Box>
      </Box>
    )
  }
}

BasePage.propTypes = {
  breadcrumbs: PropTypes.array,
  title: PropTypes.string.isRequired,
  children: PropTypes.node.isRequired,
  headerComponents: PropTypes.node,
}
