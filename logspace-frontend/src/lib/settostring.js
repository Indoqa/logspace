/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
/*
  Override toString methods. Useful for logging Flux actions for example.
  Example:
    function login() {}
    function logout() {}
    // ES6 syntax.
    setToString('user', {login, logout})
    console.log(login) // Will log 'user/login'
*/
export default function setToString(prefix: string, object: Object) {
  Object.keys(object).forEach(function(name) {
    const toStringName = prefix + '/' + name;
    object[name].toString = () => toStringName;
  });
}
