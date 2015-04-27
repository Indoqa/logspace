import {timeWindowCursor} from '../state';
import {Range, Record} from 'immutable';
import {register} from '../dispatcher';

export function getTimeWindow() {
  return timeWindowCursor();
}
