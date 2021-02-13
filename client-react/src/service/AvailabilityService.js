import axios from 'axios'
import moment from 'moment'

import localStorageService from './LocalStorageService'

const baseUrl = 'http://localhost:8080/api/availability'

const getAvailableDates = id => {
    return axios
            .get(
                `${baseUrl}/dates/${id}`, 
                { 'headers': { 'Authorization': `Bearer ${localStorageService.getToken()}`}}
            )
            .then(response => response.data)
}

const getAvailableTimes = (id, date) => {
    const formattedDate = moment(date).format("YYYY-MM-DD")
    return axios
            .get(
                `${baseUrl}/times/${id}/${formattedDate}`, 
                { 'headers': { 'Authorization': `Bearer ${localStorageService.getToken()}`}}
            )
            .then(response => response.data)
}

export default { getAvailableDates, getAvailableTimes }