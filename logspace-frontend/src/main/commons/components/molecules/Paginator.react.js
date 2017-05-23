// @flow

import React, {PropTypes} from 'react'
import {Box} from 'indoqa-react-fela'

import PaginatorItem from './PaginatorItem.react'
import Bar from './Bar.react'


export default class Paginator extends React.Component {

  static propTypes = {
    paging: PropTypes.object.isRequired,
    maxCount: PropTypes.number.isRequired,

    setPage: PropTypes.func.isRequired,
  }

  static createPageIndex(page: number, currentPage: number, maxPage: number, pageName: string = '') {
    const named = pageName !== ''
    const name = (named ? pageName : page.toString())

    const valid = page >= 0 && page <= maxPage
    const current = page === currentPage

    return {page, name, current: current && !named, visible: valid || named, enabled: valid && !current}
  }

  getPageIndexes() {
    const {paging, maxCount} = this.props
    const currentPage = parseInt(paging.start / paging.count, 10)
    const maxPage = parseInt((maxCount - 1) / paging.count, 10)

    return [
      Paginator.createPageIndex(0, currentPage, maxPage, '|<'),
      Paginator.createPageIndex(currentPage - 1, currentPage, maxPage, '<'),
      Paginator.createPageIndex(currentPage - 10, currentPage, maxPage),
      Paginator.createPageIndex(currentPage - 5, currentPage, maxPage),
      Paginator.createPageIndex(currentPage - 3, currentPage, maxPage),
      Paginator.createPageIndex(currentPage - 2, currentPage, maxPage),
      Paginator.createPageIndex(currentPage - 1, currentPage, maxPage),
      Paginator.createPageIndex(currentPage, currentPage, maxPage),
      Paginator.createPageIndex(currentPage + 1, currentPage, maxPage),
      Paginator.createPageIndex(currentPage + 2, currentPage, maxPage),
      Paginator.createPageIndex(currentPage + 3, currentPage, maxPage),
      Paginator.createPageIndex(currentPage + 5, currentPage, maxPage),
      Paginator.createPageIndex(currentPage + 10, currentPage, maxPage),
      Paginator.createPageIndex(currentPage + 1, currentPage, maxPage, '>'),
      Paginator.createPageIndex(maxPage, currentPage, maxPage, '>|'),
    ]
  }

  renderPageIndex(pageIndex: Object) {
    const action = pageIndex.enabled ? () => this.props.setPage(pageIndex.page) : () => {}

    return (
      <Box pl={1} pr={1} key={pageIndex.name}>
        <PaginatorItem
          visible={pageIndex.visible}
          current={pageIndex.current}
          enabled={pageIndex.enabled}
          onClick={action}
        >
          {pageIndex.name}
        </PaginatorItem>
      </Box>
    )
  }

  render() {
    return (
      <Bar>
        {this.getPageIndexes().map((pageIndex) => this.renderPageIndex(pageIndex))}
      </Bar>
    )
  }
}
