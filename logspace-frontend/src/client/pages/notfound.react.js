/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import DocumentTitle from 'react-document-title'
import React from 'react'
import {Link} from 'react-router'

export default class NotFound extends React.Component {

  render() {
    return (
      <DocumentTitle title={'Page Not Found'}>
        <div>
          <h1>
            {`This page isn't available`}
          </h1>
          <p>
            {'The link may be broken, or the page may have been removed.'}
          </p>
          <Link to="home">{'Continue here please.'}</Link>
        </div>
      </DocumentTitle>
    )
  }

}
