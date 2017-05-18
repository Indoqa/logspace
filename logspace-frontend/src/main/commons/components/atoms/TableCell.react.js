// @flow

import {createComponent} from 'react-fela'

const TableCell = ({theme, align}) => ({
  textAlign: align || 'left',
  padding: theme.spacing.space1,
})

export default createComponent(TableCell, 'td', ['colSpan', 'rowSpan'])
