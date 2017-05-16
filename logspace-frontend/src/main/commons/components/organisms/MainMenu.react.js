// @flow

import React from 'react'
import {Box} from 'indoqa-react-fela'
import {Link} from 'react-router'
import Logo from '../molecules/Logo.fela'
import Menu from '../molecules/Menu.fela'
import MenuLink from '../molecules/MenuLink.fela'

const MainMenu = () => (
  <Menu>
    <Logo>
      Logspace
    </Logo>
    <Box mt={2} />
    <MenuLink>
      <Link to="/"> Module 1 </Link>
    </MenuLink>
    <MenuLink>
      <Link to="/module2"> Module 2 </Link>
    </MenuLink>
    <MenuLink>
      <Link to="/agents"> Agents </Link>
    </MenuLink>
    <MenuLink>
      <Link to="/dashboards"> Dashboard </Link>
    </MenuLink>
    <MenuLink>
      <Link to="/reports"> Reports </Link>
    </MenuLink>
    <MenuLink>
      <Link to="/alerts"> Alerts </Link>
    </MenuLink>
    <MenuLink>
      <Link to="/users"> Users </Link>
    </MenuLink>
  </Menu>
)

export default MainMenu
