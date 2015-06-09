/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import Component from './component.react';
import React from 'react';

// Higher order component for exposing router.
// https://medium.com/@dan_abramov/mixins-are-dead-long-live-higher-order-components-94a0d2f9e750
export default function exposeRouter(BaseComponent) {

  class ExposeRouter extends Component {
    render() {
      return <BaseComponent {...this.props} router={this.context.router} />;
    }
  }

  ExposeRouter.contextTypes = {
    router: React.PropTypes.func.isRequired
  };

  ExposeRouter.displayName = `${BaseComponent.name}ExposeRouter`;

  return ExposeRouter;

}

