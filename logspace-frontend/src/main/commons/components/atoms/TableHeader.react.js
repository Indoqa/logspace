// @flow

import {createComponent} from 'react-fela'

const TableHeader = ({theme}) => ({
  backgroundColor: theme.colors.bgLight,
})

export default createComponent(TableHeader, 'thead')
