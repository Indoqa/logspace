import * as actions from './actions';
import {timeWindowCursor} from '../state';
import {register} from '../dispatcher';
import {Dispatcher} from 'flux';

export function getTimeWindow() {
  return timeWindowCursor();
}
  
export const TimeWindowStore_dispatchToken = register(({action, data}) => {

  switch (action) {
    case actions.onTimeWindowChange:
      timeWindowCursor(timeWindow => {
        return timeWindow.set("start", data.start);
      });
      timeWindowCursor(timeWindow => {
        return timeWindow.set("end", data.end);
      });
      timeWindowCursor(timeWindow => {
        return timeWindow.set("gap", data.gap);
      });
      break;
  }

});  
