import { toast, Zoom } from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css'

const success = (message) => {
    toast.success(String(message), {
        position: "bottom-center",
        autoClose: 5000,
        hideProgressBar: true,
        closeOnClick: true,
        pauseOnHover: false,
        draggable: true,
        progress: undefined,
        transition: Zoom
        });
}

const error = (message) => {
    toast.error(message, {
        position: "bottom-center",
        autoClose: 5000,
        hideProgressBar: true,
        closeOnClick: true,
        pauseOnHover: false,
        draggable: true,
        progress: undefined,
        transition: Zoom
        });
}

export default { success, error }