/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

 const baseColors = {
  black: '#222',
  white: '#fff',
  gray: '#ddd',
  midgray: '#B2B2B2',
  lightgray: '#f3f3f3',
  blue: '#0097a7',
  red: '#dc1b67',
  orange: '#f70',
  green: '#006064'
}

const scale = [
  0,
  8,
  16,
  32,
  64,
  128
]
const fontSizes = [
  48,
  32,
  24,
  20,
  16,
  14,
  12
]

const customTheme = {
  name: 'Logspace Theme',
  backgroundColor: '#fff',
  scale,
  fontSizes,
  colors: {
    ...baseColors,
    primary: baseColors.blue,
    secondary: baseColors.midgray,
    default: baseColors.black,
    info: baseColors.blue,
    success: baseColors.green,
    warning: baseColors.orange,
    error: baseColors.red
  },

  FontAwesome: {
    fontSize: fontSizes[2]
  },

  RefreshLoading: {
    color: '#ff0000'
  },

  Header: {
    backgroundColor: baseColors.green,
    color: baseColors.white,
    padding: scale[1],
    height: scale[3] + 2 * scale[1],
  },

  MainNavigation: {
    fontSize: fontSizes[3],
    marginRight: scale[1],
    cursor: 'pointer',
  },

  NavItemDisabled: {
    color: baseColors.secondary
  },

  AutoPlayButton: {
    width: scale[3],
    height: scale[3],
    padding: scale[0],
  },

  ReloadButton: {
    width: scale[3],
    height: scale[3],
    padding: scale[0],
  },

  ChartTypeButton: {
    width: scale[5],
    height: scale[3],
    textAlign: 'end',
    paddingTop: scale[0],
    paddingLeft: scale[1],
    paddingBottom: scale[0],
    paddingRight: scale[1],
    minHeight: '0px',
    textTransform: 'uppercase',
  },

  DurationButton: {
    width: scale[5],
    height: scale[3],
    textAlign: 'end',
    paddingTop: scale[0],
    paddingLeft: scale[1],
    paddingBottom: scale[0],
    paddingRight: scale[1],
    minHeight: '0px',
  },

  FontAwesomeToggle: {
    fontSize: fontSizes[3],
    marginLeft: scale[1],
  },

  Breadcrumbs: {
    fontSize: fontSizes[4]
  },
}

export default customTheme
