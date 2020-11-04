import axios from 'axios'

import localStorageService from './LocalStorageService'

const baseUrl = 'http://localhost:8080/api/vehicle'

const getVehiclesForUser = () => {
    return axios
            .get(
                baseUrl, 
                { 'headers': { 'Authorization': `Bearer ${localStorageService.getToken()}`}}
            )
            .then(response => response.data)
}

const deleteVehicle = (id) => {
    return axios
            .delete(
                `${baseUrl}/${id}`, 
                { 'headers': { 'Authorization': `Bearer ${localStorageService.getToken()}`}}
            )
            .then(response => response.data)
}

const registerVehicle = newVehicleInformation => {
    return axios
            .post(
                baseUrl,
                newVehicleInformation,
                { 'headers': { 'Authorization': `Bearer ${localStorageService.getToken()}`}}
            ).then(response => response.data)
}

export default { getVehiclesForUser, deleteVehicle, registerVehicle }