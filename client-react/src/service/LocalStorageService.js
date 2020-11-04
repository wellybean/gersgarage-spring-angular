
const saveToken = (data) => {
    window.localStorage.setItem("accessToken", data.accessToken)
    window.localStorage.setItem("roles", data.roles)
    window.localStorage.setItem("username", data.username)
    window.localStorage.setItem("email", data.email)
}

const removeToken = () => {
    window.localStorage.clear()
}

const getToken = () => {
    return window.localStorage.getItem("accessToken")
}

const isTokenSet = () => {
    const token = window.localStorage.getItem("accessToken")
    if(token === null || token === "") {
        return false
    } else {
        return true
    }
}

export default { saveToken, removeToken, isTokenSet, getToken }