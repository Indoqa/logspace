import {timeWindowCursor} from '../state';
import {Range, Record} from 'immutable';
import {register} from '../dispatcher';

const TodoItem = Record({
  id: '',
  title: ''
});

export const dispatchToken = register(({action, data}) => {
});

export function getTimeWindow() {
  return timeWindowCursor();
}
