// @flow

import * as R from 'ramda'

import React from 'react'
import Select from '../atoms/Select.react'
import Option from '../atoms/Option.react'

import type {DropdownOption} from '../../types/DropdownOption'


type Props = {
  options: Array<DropdownOption>,
  selectedValue: any,
  onChange: Function,
};

const getOptionName = (option: DropdownOption) => {
  if (!option.name) {
    return option.value
  }

  return option.name
}

const renderOption = (option: DropdownOption, index: number) => (
  <Option key={index} value={index}>{getOptionName(option)}</Option>
)

const getSelectedIndex = (options: Array<DropdownOption>, selectedValue: any) => (
  R.indexOf(selectedValue, options.map((option) => option.value))
)

const Dropdown = ({options, selectedValue, onChange}: Props) => (
  <Select value={getSelectedIndex(options, selectedValue)} onChange={(e) => onChange(options[e.target.value].value)}>
    {options.map(renderOption)}
  </Select>
)

export default Dropdown
