import React from 'react'

import '../style/NavBar.css'
import { makeStyles } from '@material-ui/core/styles'

import AppBar from '@material-ui/core/AppBar'
import Toolbar from '@material-ui/core/Toolbar'
import Typography from '@material-ui/core/Typography'
import Button from '@material-ui/core/Button'
import IconButton from '@material-ui/core/IconButton'
import MenuIcon from '@material-ui/icons/Menu'

import authService from '../service/AuthService'

import { Link } from 'react-router-dom'

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
  },
  menuButton: {
    marginRight: theme.spacing(2),
  },
  title: {
    flexGrow: 1,
  },
}))

export default function NavBar({ loggedStatusChange, loggedStatus }) {

  const classes = useStyles();

  return (
    <div className={classes.root}>
      <AppBar position="static">
        <Toolbar>
          <IconButton edge="start" className={classes.menuButton} color="inherit" aria-label="menu">
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" className={classes.title}>
            Ger's garage
          </Typography>
          { 
            loggedStatus ? 
            <LoggedInButtons loggedStatusChange={loggedStatusChange} /> : 
            <LoggedOutButtons loggedStatusChange={loggedStatusChange} /> 
          }
        </Toolbar>
      </AppBar>
    </div>
  )
}

export function LoggedOutButtons(props) {
  return(
    <div>
      <Link to="/login">
        <Button color="inherit">Login</Button>
      </Link>
      <Link to="/signup">
        <Button color="inherit">Signup</Button>
      </Link>
    </div>
  )
}

export function LoggedInButtons({ loggedStatusChange }) {

  const handleClickLogOut = (event) => {
    authService.logout()
    loggedStatusChange()
  }

  return(
    <div>
      Hello, {window.localStorage.getItem('username')}
      <Link to="/home">
        <Button onClick={handleClickLogOut} color="inherit">Log out</Button>
      </Link>
    </div>
  )
}