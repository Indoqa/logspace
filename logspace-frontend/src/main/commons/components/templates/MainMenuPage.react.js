import React, {PropTypes} from 'react'
import {Box, Flex} from 'indoqa-react-fela'
import Content from '../molecules/Content.fela'
import MainMenu from '../organisms/MainMenu.react'

const MainMenuPage = ({header, children}) => (
  <Flex stretch height={'100%'}>
    <MainMenu />
    <Box grow={1}>
      {header}
      <Content grow={1}>
        {children}
      </Content>
    </Box>
  </Flex>
)

MainMenuPage.propTypes = {
  header: PropTypes.element,
  children: PropTypes.element,
}

MainMenuPage.defaultProps = {
  header: null,
  children: null,
}

export default MainMenuPage
