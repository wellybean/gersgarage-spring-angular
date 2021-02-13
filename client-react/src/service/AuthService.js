import axios from 'axios'

import localStorageService from './LocalStorageService'

const baseUrl = 'http://localhost:8080/api/auth/'

const login = (credentials) => {
    return axios
            .post(baseUrl + 'signin', credentials)
            .then(
                response => {
                    if(response.data.accessToken) {
                        localStorageService.saveToken(response.data)
                    }
                    return response.data
                }
            )
}

const signUp = (newUserData) => {
    return axios
            .post(baseUrl + 'signup', newUserData)
            .then(
                response => {
                    return response.data
                }
            )
}

const isTokenValid = () => {
    return axios.get(baseUrl + 'validateToken').then(response => response.data)
}

const logout = () => {
    localStorageService.removeToken()
}

const isLoggedIn = () => {
    return localStorageService.isTokenSet()
}

export default { login, signUp, logout, isLoggedIn, isTokenValid }