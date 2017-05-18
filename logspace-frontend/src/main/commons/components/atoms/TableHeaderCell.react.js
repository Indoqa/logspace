// @flow

import {createComponent} from 'react-fela'

const TableHeaderCell = ({theme, width}) => ({
  padding: theme.spacing.space1,
  width: width || 50,
})

export default createComponent(TableHeaderCell, 'th')
