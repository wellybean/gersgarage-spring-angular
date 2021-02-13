import axios from 'axios'

import localStorageService from './LocalStorageService'

const baseUrl = 'http://localhost:8080/api/booking'

const getBookingsForUser = () => {
    return axios
            .get(
                baseUrl, 
                { 'headers': { 'Authorization': `Bearer ${localStorageService.getToken()}`}}
            )
            .then(response => response.data)
}

const cancelBooking = (id) => {
    return axios
            .delete(
                `${baseUrl}/${id}`, 
                { 'headers': { 'Authorization': `Bearer ${localStorageService.getToken()}`}}
            )
            .then(response => response.data)
}

const makeBooking = newBookingInformation => {
    return axios
            .post(
                baseUrl,
                newBookingInformation,
                { 'headers': { 'Authorization': `Bearer ${localStorageService.getToken()}`}}
            ).then(response => response.data)
}

export default { getBookingsForUser, cancelBooking, makeBooking }