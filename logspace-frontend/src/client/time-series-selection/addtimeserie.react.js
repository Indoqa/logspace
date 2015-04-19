import React from 'react';
import PureComponent from '../components/purecomponent.react';

export default class AddTimeSerie extends PureComponent {

  foo() {
    alert('foo');
  }

  render() {
    return (
      <div>
			  <button onClick={() => this.foo()}>add time serie</button>
			</div>
    );
  }

}
