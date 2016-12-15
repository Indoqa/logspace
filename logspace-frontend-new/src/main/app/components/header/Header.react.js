/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'
import {Link} from 'react-router'
import {Base, Breadcrumbs, Dropdown, DropdownMenu, NavItem} from 'rebass'
import {Span, FontAwesome} from 'indoqa-rebass-components'
import {Flex} from 'reflexbox'
import {title} from '../../../environment'
import * as routes from '../../../routes'

import 'font-awesome/css/font-awesome.css'

export default class Header extends React.Component {

  constructor(props) {
    super(props)
    this.state = {navigationShown: false}
  }

  toggleNavigationShown() {
    const currentValue = this.state.navigationShown
    this.setState({navigationShown: !currentValue})
  }

  createBreadcrumb(link) {
    return {children: link.children, is: Link, to: link.to}
  }

  createBreadcrumbLinks() {
    if (!this.props.breadcrumbs || this.props.breadcrumbs.length === 0) {
      return [this.createBreadcrumb({children: title('Logspace')})]
    }

    return [this.createBreadcrumb({children: title('Logspace'), is: Link, to: '/'})]
      .concat(this.props.breadcrumbs.map(link => this.createBreadcrumb(link)))
  }

  createNavItem(route, name) {
    if (this.context.location.pathname === route) {
      return <NavItem style={{cursor: 'default'}}><Span style={{cursor: 'default'}}>{name}</Span></NavItem>
    }

    return <NavItem is={Link} to={route} > {name} </NavItem>
  }

  render() {
    return (
      <Base className="Header">
        <Flex align="center" style={{height: '100%'}}>
          <Dropdown>
            <FontAwesome icon="bars" className="MainNavigation" onClick={() => this.toggleNavigationShown()} />
            <DropdownMenu open={this.state.navigationShown} onDismiss={() => this.toggleNavigationShown()}>
              {this.createNavItem(routes.ROUTE_OVERVIEW, 'Overview')}
              {this.createNavItem(routes.ROUTE_AGENTS, 'Agents')}
              {this.createNavItem(routes.ROUTE_DASHBOARDS, 'Dashboards')}
              {this.createNavItem(routes.ROUTE_REPORTS, 'Reports')}
              {this.createNavItem(routes.ROUTE_ALERTS, 'Alerts')}
              {this.createNavItem(routes.ROUTE_USERS, 'Users')}
            </DropdownMenu>
          </Dropdown>
          <Breadcrumbs links={this.createBreadcrumbLinks()} m={0} />
          {this.props.children}
        </Flex>
      </Base>
    )
  }
}

Header.propTypes = {
  children: PropTypes.node,
  breadcrumbs: PropTypes.array.isRequired,
}

Header.contextTypes = {
  location: PropTypes.object,
}
