/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
// Create a nice client unique enough id.
// https://github.com/google/closure-library/blob/2012c5372fdb02ce9531cf4b2561b05e3ce2ab39/closure/goog/string/string.js#L1183
export function getRandomString() {
  var x = 2147483648;
  return Math.floor(Math.random() * x).toString(36) +
         Math.abs(Math.floor(Math.random() * x) ^ Date.now()).toString(36);
}
