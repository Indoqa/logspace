/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import Validation from '../lib/validation';
import {msg} from './intl/store';

class AppValidation extends Validation {

  getRequiredMessage(prop) {
    return msg('validation.required', {prop});
  }

  getEmailMessage(prop) {
    return msg('validation.email', {prop});
  }

  getSimplePasswordMessage(minLength) {
    return msg('validation.password', {minLength});
  }

}

export function validate(object: Object) {
  return new AppValidation(object);
}
