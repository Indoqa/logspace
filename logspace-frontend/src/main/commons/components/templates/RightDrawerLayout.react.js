import React, {PropTypes} from 'react'
import {Flex, Box} from 'indoqa-react-fela'
import RightDrawer from '../molecules/RightDrawer.fela'

const RightDrawerLayout = ({active, drawer, children}) => (
  <Flex stretch height="100%">
    <Box grow={1} p={1}>
      {children}
    </Box>
    <RightDrawer active={active}>
      {drawer}
    </RightDrawer>
  </Flex>
)

RightDrawerLayout.propTypes = {
  active: PropTypes.bool,
  drawer: PropTypes.element,
  children: PropTypes.element,
}

RightDrawerLayout.defaultProps = {
  active: false,
  drawer: null,
  children: null,
}

export default RightDrawerLayout
