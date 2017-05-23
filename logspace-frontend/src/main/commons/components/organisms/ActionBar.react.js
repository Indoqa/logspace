import React, {PropTypes} from 'react'
import {Box} from 'indoqa-react-fela'
import Bar from '../molecules/Bar.react'

const ActionBar = ({left, center, right}) => (
  <Bar pl={1} pr={1}>
    <Box>
      {left}
    </Box>
    <Box grow={1} />
    <Box>
      {center}
    </Box>
    <Box grow={1} />
    <Box>
      {right}
    </Box>
  </Bar>
)

ActionBar.propTypes = {
  left: PropTypes.element,
  center: PropTypes.element,
  right: PropTypes.element,
}

ActionBar.defaultProps = {
  left: null,
  center: null,
  right: null,
}

export default ActionBar
