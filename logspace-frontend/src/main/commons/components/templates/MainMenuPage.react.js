import React, {PropTypes} from 'react'
import {Box, Flex} from 'indoqa-react-fela'
import Bar from '../molecules/Bar.fela'
import Content from '../molecules/Content.fela'
import MainMenu from '../organisms/MainMenu.react'

const MainMenuPage = ({title, header, children}) => (
  <Flex stretch height={'100%'}>
    <MainMenu />
    <Box grow={1}>
      <Bar pl={1} pr={1}>
        <Box>{title}</Box>
        <Box grow={1} />
        <Box>{header}</Box>
      </Bar>
      <Content grow={1}>
        {children}
      </Content>
    </Box>
  </Flex>
)

MainMenuPage.propTypes = {
  title: PropTypes.string,
  header: PropTypes.element,
  children: PropTypes.element,
}

MainMenuPage.defaultProps = {
  title: '',
  header: null,
  children: null,
}

export default MainMenuPage
