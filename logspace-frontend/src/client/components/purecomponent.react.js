/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react';
import shallowEqual from 'react/lib/shallowEqual';

/**
 * PureRenderMixin replacement for React component ES6 classes.
 * https://github.com/facebook/react/blob/ed3e6ecb9b86b97c09428f40deb8c3ed695e73e8/src/addons/ReactComponentWithPureRenderMixin.js
 */
export default class PureComponent extends React.Component {

  shouldComponentUpdate(nextProps, nextState) {
    return !shallowEqual(this.props, nextProps) ||
           !shallowEqual(this.state, nextState);
  }

}
