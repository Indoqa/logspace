// @flow

import React from 'react'
import {Box} from 'indoqa-react-fela'
import Action from '../../../commons/components/molecules/Action.fela'
import ActionBar from '../../../commons/components/organisms/ActionBar.react'
import MainMenuPage from '../../../commons/components/templates/MainMenuPage.react'
import RightDrawerLayout from '../../../commons/components/templates/RightDrawerLayout.react'

const renderSettings = () => (
  <Box>
    module 1 settings
  </Box>
)

class Module1Page extends React.Component {
  state = {
    showSettings: true,
  };

  toggleSettings() {
    this.setState({showSettings: !this.state.showSettings})
  }

  renderActionBar() {
    const toggleSettings = (
      <Action onClick={() => this.toggleSettings()}> Toggle Settings </Action>
    )

    return (
      <ActionBar right={toggleSettings} />
    )
  }

  render() {
    return (
      <MainMenuPage header={this.renderActionBar()}>
        <RightDrawerLayout active={this.state.showSettings} drawer={renderSettings()}>
          Content of module 1
        </RightDrawerLayout>
      </MainMenuPage>
    )
  }
}

export default Module1Page
