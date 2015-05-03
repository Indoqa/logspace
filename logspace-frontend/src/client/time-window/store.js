import * as actions from './actions';
import {timeWindowCursor} from '../state';
import {register} from '../dispatcher';
import {Dispatcher} from 'flux';

export function getTimeWindow() {
  return timeWindowCursor().get("data");
}
  
export const TimeWindowStore_dispatchToken = register(({action, data}) => {

  switch (action) {
    case actions.onTimeWindowChange:
      timeWindowCursor(timeWindow => {
        return timeWindow.set("data", data);
      });
      break;
  }

});  
