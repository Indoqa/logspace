// @flow

import {createSelector} from 'reselect'
import {selectAgentsState} from '../../../selectors'

import type {AgentsState} from '../types/AgentsState'


export const selectAutoReload = createSelector(selectAgentsState, (state: AgentsState) => state.autoReload)
export const selectPaging = createSelector(selectAgentsState, (state: AgentsState) => state.paging)
export const selectSorting = createSelector(selectAgentsState, (state: AgentsState) => state.sorting)
export const selectInterval = createSelector(selectAgentsState, (state: AgentsState) => state.interval)
export const selectResult = createSelector(selectAgentsState, (state: AgentsState) => state.result)
export const selectError = createSelector(selectAgentsState, (state: AgentsState) => state.error)
