import {dispatch} from '../dispatcher';
import setToString from '../../lib/settostring';

export function onTimeWindowChange(newTimeWindowData) {
  dispatch(onTimeWindowChange, newTimeWindowData);
}

// Override actions toString for logging.
setToString('timeWindow', {
  onTimeWindowChange
});
  