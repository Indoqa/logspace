import React, {PropTypes} from 'react'

const Buttons = ({loadVienna, loadNewYork, loadInvalidLocation, clear}) => (
  <div>
    <div>
      <button onClick={loadVienna}> Vienna </button>
      <button onClick={loadNewYork}> New York </button>
      <button onClick={loadInvalidLocation}> Invalid Location </button>
      <button onClick={clear}> Clear </button>
    </div>
  </div>
)

Buttons.propTypes = {
  loadVienna: PropTypes.func,
  loadNewYork: PropTypes.func,
  loadInvalidLocation: PropTypes.func,
  clear: PropTypes.func
}

export default Buttons
