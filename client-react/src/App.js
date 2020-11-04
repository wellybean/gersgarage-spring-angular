import React, { useState } from 'react';

import './App.css';

import NavBar from './component/NavBar';
import Home from './component/Home';
import Login from './component/Login';
import Signup from './component/Signup';
import UserHome from './component/UserHome';

import authService from './service/AuthService'

import { Switch, Redirect, Route } from 'react-router-dom'

 const App = () => {
  
  const [loggedStatus, setLoggedStatus] = useState(authService.isLoggedIn())

  const loggedStatusChange = () => {
    setLoggedStatus(authService.isLoggedIn())
  }

  return (
    <div>
        <NavBar loggedStatusChange={loggedStatusChange} loggedStatus={loggedStatus} />
        <Switch>
          <Route path="/home">
            <Home />
          </Route>
          <Route exact path="/">
            <Redirect to={ "/home" } />
          </Route>
          <Route path="/login">
            <Login loggedStatusChange={loggedStatusChange} />
          </Route>
          <Route path="/signup">
            <Signup />
          </Route>
          <Route path="/user-home">
            { loggedStatus ? <UserHome /> : <Redirect to="/login" /> }
          </Route>
        </Switch>
    </div>
  )
}

export default App;
