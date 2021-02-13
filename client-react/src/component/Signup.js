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

export default function Signup() {

    const classes = useStyles();

    const history = useHistory();

    const [visibility, setVisibility] = useState(false)
    const [userInformation, setUserInformation] = useState({
        firstName: '',
        lastName: '',
        email: '',
        phoneNumber: '',
        username: '',
        password: '',
        passwordConfirmation: ''
    })

    const isFormValid = () => {
        return userInformation.firstName !== '' && userInformation.lastName !== '' && userInformation.email !== '' &&
                userInformation.phoneNumber !== '' && userInformation.username !== '' && userInformation.password !== '' &&
                userInformation.passwordConfirmation !== ''
    }

    const handleClickShowPassword = (event) => {
        event.preventDefault()
        setVisibility(!visibility)
    }

    const handleChange = (prop) => event => {
        setUserInformation({ ...userInformation, [prop]: event.target.value })
    }

    const handleClickSignUp = (event) => {
        event.preventDefault()
        authService
            .signUp(userInformation)
            .then(
                () => {
                    history.push("/login")
                },
                error => console.log(error)
            )
    }

    return(
        <div className="root">
            <form>
                <br />
                <h2>Registration page</h2>
                <FormControl variant="outlined" className={classes.textField}>
                    <InputLabel htmlFor="firstName">First name</InputLabel>
                    <OutlinedInput 
                        id="firstName"
                        type="text"
                        value={userInformation.firstName}
                        onChange={handleChange('firstName')}
                        labelWidth={70}
                    />
                </FormControl>
                <br />
                <FormControl variant="outlined" className={classes.textField}>
                    <InputLabel htmlFor="lastName">Last name</InputLabel>
                    <OutlinedInput 
                        id="lastName"
                        type="text"
                        value={userInformation.lastName}
                        onChange={handleChange('lastName')}
                        labelWidth={70}
                    />
                </FormControl>
                <br />
                <FormControl variant="outlined" className={classes.textField}>
                    <InputLabel htmlFor="email">Email</InputLabel>
                    <OutlinedInput 
                        id="email"
                        type="email"
                        value={userInformation.email}
                        onChange={handleChange('email')}
                        labelWidth={70}
                    />
                </FormControl>
                <br />
                <FormControl variant="outlined" className={classes.textField}>
                    <InputLabel htmlFor="phoneNumber">Phone number</InputLabel>
                    <OutlinedInput 
                        id="phoneNumber"
                        type="text"
                        value={userInformation.phoneNumber}
                        onChange={handleChange('phoneNumber')}
                        labelWidth={70}
                    />
                </FormControl>
                <br />
                <FormControl variant="outlined" className={classes.textField}>
                    <InputLabel htmlFor="username">Username</InputLabel>
                    <OutlinedInput 
                        id="username"
                        type="text"
                        value={userInformation.username}
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
                        value={userInformation.password}
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
                <FormControl variant="outlined" className={classes.textField}>
                    <InputLabel htmlFor="passwordConfirmation">Password confirmation</InputLabel>
                    <OutlinedInput
                        id="passwordConfirmation"
                        type={visibility ? 'text' : 'password'}
                        value={userInformation.passwordConfirmation}
                        onChange={handleChange('passwordConfirmation')}
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
                <Button variant="contained" color="primary" className={classes.button} onClick={handleClickSignUp} disabled={!isFormValid()}>
                    Register
                </Button>
                <br />
                <br />
                <p>Already signed up?</p>
                <Link to="/login">
                    <Button variant="outlined" color="primary" className={classes.button}>
                        Login
                    </Button>
                </Link>      
            </form>
        </div>
    )
}