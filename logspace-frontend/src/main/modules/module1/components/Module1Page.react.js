// @flow

import React from 'react'
import {Box} from 'indoqa-react-fela'
import Action from '../../../commons/components/molecules/Action.fela'
import ActionBar from '../../../commons/components/organisms/ActionBar.react'
import DataTable from '../../../commons/components/molecules/DataTable.react'
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
    const data = {
      widths: [
        100,
        20,
        80,
      ],
      header: [
        {
          cells: [
            {content: 'h1'},
            {content: 'h2'},
            {content: 'h3'},
          ],
        }
      ],
      body: [
        {
          cells: [
            {content: 'row1_c1'},
            {content: 'row1_c2', align: 'right'},
            {content: 'row1_c3'},
          ],
        },
        {
          cells: [
            {content: 'row1_c1'},
            {content: 'row1_c2'},
            {content: 'row1_c3'},
          ],
        },
      ],
      footer: [{
        cells: [
          {content: 'f1'},
          {content: 'f2'},
          {content: 'f3'},
        ]}
      ],
    }

    return (
      <MainMenuPage header={this.renderActionBar()}>
        <RightDrawerLayout active={this.state.showSettings} drawer={renderSettings()}>
          <DataTable data={data} />
        </RightDrawerLayout>
      </MainMenuPage>
    )
  }
}

export default Module1Page
