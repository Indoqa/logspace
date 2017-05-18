// @flow

import {createComponent} from 'react-fela'

const TableFooter = ({theme}) => ({
  backgroundColor: theme.colors.bgLight,
})

export default createComponent(TableFooter, 'tfoot')
