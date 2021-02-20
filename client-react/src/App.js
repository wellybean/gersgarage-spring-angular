import React, { useState } from 'react';

import './App.css';

import NavBar from './component/NavBar';
import Home from './component/Home';
import Login from './component/Login';
import Signup from './component/Signup';
import UserHome from './component/UserHome';
import AdminHome from './component/AdminHome';

import authService from './service/AuthService'

import { Switch, Redirect, Route } from 'react-router-dom'

 const App = () => {
  
  const [loggedStatus, setLoggedStatus] = useState(authService.isLoggedIn())
  const [roles, setRoles] = useState(authService.getPriviledgesList())

  // useEffect(() => {
  //   authService.isTokenValid().then(
  //     error => authService.logout()
  //   )
  // })

  const loggedStatusChange = () => {
    setLoggedStatus(authService.isLoggedIn())
    setRoles(authService.getPriviledgesList())
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
            { loggedStatus && roles.includes("ROLE_USER") ? <UserHome /> : <Redirect to="/login" /> }
          </Route>
          <Route path="/admin-home">
            { loggedStatus && roles.includes("ROLE_ADMIN") ? <AdminHome /> : <Redirect to="/login" /> }
          </Route>
        </Switch>
    </div>
  )
}

export default App;
