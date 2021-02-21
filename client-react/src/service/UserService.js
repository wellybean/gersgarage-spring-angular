import axios from 'axios'
import localStorageService from './LocalStorageService'

const baseUrl = 'http://localhost:8080/api/user'

const getUsers = () => {
    return axios
            .get(
                baseUrl, 
                { 'headers': { 'Authorization': `Bearer ${localStorageService.getToken()}`}}
            )
            .then(response => response.data)
}

const deleteUserAccount = (id) => {
    return axios
            .delete(
                `${baseUrl}/${id}`, 
                { 'headers': { 'Authorization': `Bearer ${localStorageService.getToken()}`}}
            )
            .then(response => response.data)
}

export default { getUsers, deleteUserAccount }