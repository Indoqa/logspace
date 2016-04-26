/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

export function restUrl(url): string {
  if (typeof window.logspaceRestUrl === "function") { 
    return window.logspaceRestUrl(url);
  }  

  return url;
}

export function title(defaultTitle): string {
  if (window.logspaceTitle) {
    return window.logspaceTitle
  }  

  return defaultTitle
}

export function marginTop(): integer {
  if (window.logspaceMarginTop) {
    return window.logspaceMarginTop
  }  

  return 0
}
