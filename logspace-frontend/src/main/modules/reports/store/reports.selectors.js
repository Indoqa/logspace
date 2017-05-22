// @flow

import {createSelector} from 'reselect'

import type {ReportsState} from '../types/ReportsState'
import {selectReportsState} from '../../../selectors'

export const selectPaging = createSelector(selectReportsState, (state: ReportsState) => state.paging)
export const selectSorting = createSelector(selectReportsState, (state: ReportsState) => state.sorting)
export const selectResult = createSelector(selectReportsState, (state: ReportsState) => state.result)
export const selectError = createSelector(selectReportsState, (state: ReportsState) => state.error)
export const selectIsLoading = createSelector(selectReportsState, (state: ReportsState) => state.isLoading)
