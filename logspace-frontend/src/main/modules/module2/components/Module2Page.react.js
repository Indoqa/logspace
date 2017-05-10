// @flow

import React from 'react'
import {Box} from 'indoqa-react-fela'
import MainMenuPage from '../../../commons/components/templates/MainMenuPage.react'

class Module2Page extends React.Component {

  render() {
    return (
      <MainMenuPage>
        <Box p={4}>
          Content of module 2 without drawer
        </Box>
      </MainMenuPage>
    )
  }
}

export default Module2Page
