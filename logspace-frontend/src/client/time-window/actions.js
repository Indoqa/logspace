/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import {dispatch} from '../dispatcher';
import setToString from '../../lib/settostring';

export function onShowTimeWindowForm() {
  dispatch(onShowTimeWindowForm);
}

export function selectCustomDate(start, end, gap) {
  dispatch(selectCustomDate, {start: start, end:end, gap:gap});
}

export function selectDynamicDate(duration, unit, gap) {
  dispatch(selectDynamicDate, {duration: duration, unit:unit, gap:gap});
}

export function selectPredefinedDate(selection) {
  dispatch(selectPredefinedDate, selection);
}

export function onTabOpen(tabIndex) {
  dispatch(onTabOpen, tabIndex);
}

// Override actions toString for logging.
setToString('timeWindow', {
  selectCustomDate, selectPredefinedDate, onShowTimeWindowForm, selectDynamicDate,onTabOpen
});
  