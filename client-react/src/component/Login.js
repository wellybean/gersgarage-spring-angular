import React, { useState } from 'react'

import { makeStyles } from '@material-ui/core/styles';
import '../style/Login.css'

import FormControl from '@material-ui/core/FormControl'
import InputLabel from '@material-ui/core/InputLabel'
import OutlinedInput from '@material-ui/core/OutlinedInput'
import Visibility from '@material-ui/icons/Visibility'
import VisibilityOff from '@material-ui/icons/VisibilityOff'
import InputAdornment from '@material-ui/core/InputAdornment'
import IconButton from '@material-ui/core/IconButton'
import Button from '@material-ui/core/Button';

import { Link, useHistory } from 'react-router-dom';

import authService from '../service/AuthService'

const useStyles = makeStyles((theme) => ({
    root: {
      display: 'flex',
      flexWrap: 'wrap',
    },
    textField: {
        marginTop: theme.spacing(1),
        width: '30ch',
    },
    button: {
        width: '100%'
    }
  }));

export default function Login({ loggedStatusChange }) {

    const classes = useStyles();

    const history = useHistory();

    const [visibility, setVisibility] = useState(false)
    const [credentials, setCredentials] = useState({
        username: '',
        password: ''
    })

    const handleClickShowPassword = (event) => {
        event.preventDefault()
        setVisibility(!visibility)
    }

    const handleChange = (prop) => event => {
        setCredentials({ ...credentials, [prop]: event.target.value })
    }

    const handleClickLogin = (event) => {
        event.preventDefault()
        authService
            .login(credentials)
            .then(
                () => {
                    loggedStatusChange()
                    history.push("/user-home")
                },
                error => console.log(error)
            )
    }

    return(
        <div className="root">
            <form>
                <h2>Login</h2>
                <FormControl variant="outlined" className={classes.textField}>
                    <InputLabel htmlFor="username">Username</InputLabel>
                    <OutlinedInput 
                        id="username"
                        type="text"
                        value={credentials.username}
                        onChange={handleChange('username')}
                        labelWidth={70}
                    />
                </FormControl>
                <br />
                <FormControl variant="outlined" className={classes.textField}>
                    <InputLabel htmlFor="password">Password</InputLabel>
                    <OutlinedInput
                        id="password"
                        type={visibility ? 'text' : 'password'}
                        value={credentials.password}
                        onChange={handleChange('password')}
                        endAdornment={
                        <InputAdornment position="end">
                            <IconButton
                            aria-label="toggle password visibility"
                            onClick={handleClickShowPassword}
                            edge="end"
                            >
                            {visibility ? <Visibility /> : <VisibilityOff />}
                            </IconButton>
                        </InputAdornment>
                        }
                        labelWidth={70}
                    />
                </FormControl>
                <br />
                <br />
                <Button variant="contained" color="primary" className={classes.button} onClick={handleClickLogin}>
                    Login
                </Button>
                <br />
                <br />
                <p>Not yet signed up?</p>
                <Link to="/signup">
                    <Button variant="outlined" color="primary" className={classes.button}>
                        Sign up
                    </Button>
                </Link>
            </form>
        </div>
    )
}