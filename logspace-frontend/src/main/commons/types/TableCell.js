// @flow

import {FelaComponent} from 'react-fela'

export type TableCell = {
  content: Element | FelaComponent | string;
  colspan?: number;
  rowspan?: number;
  align?: string;
}
