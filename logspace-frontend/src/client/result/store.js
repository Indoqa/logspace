import * as timeWindowActions from '../time-window/actions';
import {TimeWindowStore_dispatchToken, getTimeWindow} from '../time-window/store';
import {resultCursor} from '../state';
import {register,waitFor} from '../dispatcher';

export function getResult() {
  return resultCursor();
}
  
export const ResultStore_dispatchToken = register(({action, data}) => {
  switch (action) {
    case timeWindowActions.onTimeWindowChange:
      waitFor(TimeWindowStore_dispatchToken);
      refreshResult();  
      break;
  }

});  

function refreshResult() {
  // TODO: execute ajax call  
  alert('executing ajax');
  
  resultCursor(result => {
        return result.set("data", Math.random());
  });
}
