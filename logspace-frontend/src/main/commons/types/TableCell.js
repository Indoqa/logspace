// @flow

export type TableCell = {
  content: Element | string;
  colspan?: number;
  rowspan?: number;
  align?: string;
}
