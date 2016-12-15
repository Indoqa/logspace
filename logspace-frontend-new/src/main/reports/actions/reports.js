/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
 import {fetchLogspace} from '../../apis'

 const REPORT_PREFIX = 'REPORT|'

 export const LOAD_REPORTS = `${REPORT_PREFIX}LOAD_REPORTS`

 export const loadReports = () => ({getState}) => {
   const start = getState().reports.get('start')
   const count = getState().reports.get('count')

   return {
     type: LOAD_REPORTS,
     payload: fetchLogspace(`/reports?start=${start}&count=${count}`)
   }
 }
