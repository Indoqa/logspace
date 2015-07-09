/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import messages from './messages';
import {selections, units} from './time-window/constants'
import Immutable from 'immutable'

const initialLocale = 'en';

export default {
  i18n: {
    formats: {},
    locales: initialLocale,
    messages: messages[initialLocale]
  },
  'timeWindow': {
    selection: selections[0],
    activeTab: 1,
    dynamic: {
      range: {
        amount: 60,
        unit: units.get('minute')  
      },
      gap: {
        amount: 1,
        unit: units.get('minute')  
      }
    }
  },
  'timeSeries': [],
  'timeSeriesDefaults': {
    propertyStack: [],
    aggregate: 'count'
  },
  'editedTimeSeries': {},
  'result': {
    'translatedResult': {
      'error': false,
      'empty': true,
      'series': null,
      'xvalues': [],
      'warnings': []
    },
    'chartTitle': 'New Chart',
    'chartType': 'spline',
    'autoPlay': false
  },
  'suggestions': {
    'request' : {
      'text': null,
      'system': null,
      'space': null,
      'propertyId': null
    },
    'result': {
      'spaces': [],
      'systems': [],
      'propertyNames': [],
      'agentDescriptions': [],
      'loading': false
    }
  },
  'view': {
    'activePanel': null,
    'navDrawerCss': {
      'navigation-drawer' : true,
      'navigation-drawer-expanded' : false
    },
    'mainCss': {
      'main' : true,
      'main-reduced' : false
    },
    'editables': {
      'result': {
        'chartTitle': {
          isEdited: false
        }
      }
    }
  }
}
