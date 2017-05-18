// @flow
import type {TableRow} from './TableRow'

export type TableData = {
  widths: number[];
  header: TableRow[];
  body: TableRow[];
  footer: TableRow[];
}
