// @flow

import React from 'react'
import Table from '../atoms/Table.react'
import TableHeader from '../atoms/TableHeader.react'
import TableHeaderCell from '../atoms/TableHeaderCell.react'
import TableBody from '../atoms/TableBody.react'
import TableFooter from '../atoms/TableFooter.react'
import TableRow from '../atoms/TableRow.react'
import TableCell from '../atoms/TableCell.react'

import type {TableData} from '../../types/TableData'

type Props = {
  data: TableData;
};

const renderHeaderRow = (row, widths) => (
  <TableRow>
    {row.cells.map((cell, index) => (
      <TableHeaderCell width={widths[index]}> {cell.content} </TableHeaderCell>
    ))}
  </TableRow>
)

const renderRow = (row) => (
  <TableRow>
    {row.cells.map((cell) => (
      <TableCell align={cell.align}> {cell.content} </TableCell>
    ))}
  </TableRow>
)

const DataTable = ({data}: Props) => (
  <Table>
    <TableHeader>
      {data.header.map((row) => renderHeaderRow(row, data.widths))}
    </TableHeader>
    <TableBody>
      {data.body.map((row) => renderRow(row))}
    </TableBody>
    <TableFooter>
      {data.footer.map((row) => renderRow(row))}
    </TableFooter>
  </Table>
)

export default DataTable
