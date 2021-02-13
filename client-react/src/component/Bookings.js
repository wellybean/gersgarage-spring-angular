import { Table, TableBody, TableContainer, TableHead, TableRow, TableCell, Typography, Button, Modal, FormControl, InputLabel, TextField, Select, MenuItem } from "@material-ui/core"
import React, { useEffect, useState } from "react"
import DeleteIcon from '@material-ui/icons/Delete'
import { makeStyles } from '@material-ui/core/styles';
import vehicleService from "../service/VehicleService"
import serviceService from "../service/ServiceService"
import bookingService from "../service/BookingService"
import availabilityService from "../service/AvailabilityService"
import { useHistory } from "react-router-dom"
import moment from "moment";
import notificationService from '../service/NotificationService'

const useStyles = makeStyles((theme) => ({
    table: {
        maxWidth: 650,
        margin: "auto",
    },
    modal: {
        backgroundColor: "white",
        position: "absolute",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%)",
        width: 400,
        height: 600,
        border: '2px solid #000',
        boxShadow: theme.shadows[5],
        padding: theme.spacing(2, 4, 3),
    },
    textField: {
        marginTop: theme.spacing(1),
        width: "100%"
    },
    select: {
        marginTop: theme.spacing(1),
        width: "100%"
    }, 
    tableHead: {
        fontWeight: "bold"
    }, panel: {
        textAlign: "center"
    }
}));

export default function Bookings() {

    const history = useHistory();

    const [vehicles, setVehicles] = useState([])
    const [services, setServices] = useState([])
    const [bookings, setBookings] = useState([])
    const [availableDates, setAvailableDates] = useState([])
    const [availableTimes, setAvailableTimes] = useState([])
    const [bookingModalOpen, setBookingModalOpen] = useState(false)
    const classes = useStyles()
    const [newBookingInformation, setNewBookingInformation] = useState({
        vehicle: {
            id: ''
        },
        service: {
            id: ''
        },
        date: '',
        time: '',
        comments: ''
    })

    const isFormValid = () => {
        return newBookingInformation.vehicle.id !== '' && newBookingInformation.service.id !== '' && newBookingInformation.date !== '' && 
        newBookingInformation.time !== ''
    }

    useEffect(() => {
        vehicleService.getVehiclesForUser().then(
            response => {
                setVehicles(response)
            },
            () => notificationService.error("Unable to obtain vehicles information.")
        )
        serviceService.getServices().then(
            response => setServices(response),
            () => notificationService.error("Unable to obtain services information.")
        )
        bookingService.getBookingsForUser().then(
            response => {
                setBookings(response)
                console.log(response)
            },
            () => notificationService.error("Unable to obtain bookings information.")
        )
    },[history])

    const handleClickCancelBooking = id => {
        bookingService
            .cancelBooking(id)
            .then(() => {
                    setBookings(bookings.filter(booking => booking.id !== id))
                    notificationService.success("Booking cancelled successfully.")
                }, 
                () => notificationService.error("Unable to cancel bookings.")
            )
    }

    const handleClickOpenBookingModal = event => {
        event.preventDefault()
        setBookingModalOpen(true)
    }

    const handleClickMakeBooking = event => {
        event.preventDefault()
        bookingService
            .makeBooking(newBookingInformation)
            .then(
                newBooking => {
                    setBookings(bookings.concat(newBooking))
                    clearAndCloseModal()
                    notificationService.success("Maintenance service booked successfully.")
                },
                () => {
                    clearAndCloseModal()
                    notificationService.error("Unable to book maintenance service.")
                }
            )
    }

    const handleCloseBookingModal = () => {
        setBookingModalOpen(false)
    }

    const handleChange = (prop) => event => {

        const structure = prop.split(".")

        if (structure.length === 1) {
            setNewBookingInformation({ ...newBookingInformation, [prop]: event.target.value })

            if (prop === "date") {
                availabilityService.getAvailableTimes(newBookingInformation.service.id, event.target.value).then(
                    response => {
                        setAvailableTimes(response)
                    },
                    () => notificationService.error("Unable to obtain available dates")
                )
            }
        } else {
            let propDetails = newBookingInformation[structure[0]]
            propDetails = { ...propDetails, [structure[1]]: event.target.value }
            setNewBookingInformation({ ...newBookingInformation, [structure[0]]: propDetails })

            if (structure[0] === "service") {
                availabilityService.getAvailableDates(event.target.value).then(
                    response => setAvailableDates(response),
                    () => notificationService.error("Unable to obtain available times")
                )
            }
        }
    }

    const clearAndCloseModal = () => {
        setNewBookingInformation({ vehicle: { id: "" }, service: { id: "" }, date: "", time: "", comments: "" })
        setBookingModalOpen(false)
    }

    return (
        <div className={classes.panel}>
            <Button
                variant="contained"
                color="primary"
                onClick={handleClickOpenBookingModal}
                size="small"
            >
                Make a booking
            </Button>
            <Modal
                open={bookingModalOpen}
                onClose={handleCloseBookingModal}
                aria-labelledby="simple-modal-title"
                aria-describedby="simple-modal-description"
            >
                <div className={classes.modal}>
                    <form>
                        <h2>Booking information: </h2>
                        <FormControl variant="outlined" className={classes.select}>
                            <InputLabel htmlFor="vehicle">Vehicle</InputLabel>
                            <Select
                                id="vehicle"
                                type="select"
                                value={newBookingInformation.vehicle.id}
                                onChange={handleChange('vehicle.id')}
                                labelWidth={100}
                            >
                                {
                                    vehicles.map(vehicle => <MenuItem value={vehicle.id} key={vehicle.id}>{vehicle.make + " " + vehicle.model + " - " + vehicle.licensePlate}</MenuItem>)
                                }
                            </Select>
                        </FormControl>
                        <br />
                        <FormControl variant="outlined" className={classes.textField}>
                            <InputLabel htmlFor="service">Service</InputLabel>
                            <Select
                                id="service"
                                type="select"
                                value={newBookingInformation.service.id}
                                onChange={handleChange('service.id')}
                                labelWidth={100}
                            >
                                {
                                    services.map(service => <MenuItem key={service.id} value={service.id}>{service.description + " (€" + service.price + " - " + service.durationInMinutes + " min)"}</MenuItem>)
                                }
                            </Select>
                        </FormControl>
                        <br />
                        <FormControl variant="outlined" className={classes.textField}>
                            <InputLabel htmlFor="date">Date</InputLabel>
                            <Select
                                id="date"
                                type="select"
                                value={newBookingInformation.date}
                                onChange={handleChange('date')}
                                labelWidth={100}
                            >
                                {
                                    availableDates.map(date => <MenuItem key={date} value={date}>{moment(date).format("(ddd) Do MMM YYYY")}</MenuItem>)
                                }
                            </Select>
                        </FormControl>
                        <br />
                        <FormControl variant="outlined" className={classes.textField}>
                            <InputLabel htmlFor="time">Time</InputLabel>
                            <Select
                                id="time"
                                type="select"
                                value={newBookingInformation.time}
                                onChange={handleChange('time')}
                                labelWidth={100}
                            >
                                {
                                    availableTimes.map(time => <MenuItem key={time} value={time}>{moment(time, "HH:mm:ss").format("HH:mm")}</MenuItem>)
                                }
                            </Select>
                        </FormControl>
                        <br />
                        <FormControl variant="outlined" className={classes.select}>
                            <TextField
                                id="comments"
                                label="Comments"
                                multiline
                                rows={4}
                                defaultValue=""
                                variant="outlined"
                                labelWidth={100}
                            />
                        </FormControl>
                        <br />
                        <br />
                        <Button variant="contained" color="primary" className={classes.button} onClick={handleClickMakeBooking} disabled={!isFormValid()}>
                            Register
                         </Button>
                    </form>
                </div>
            </Modal>
            <br />
            <br />
            {
                bookings.length === 0 ?
                    <Typography>No bookings available</Typography> :
                    <TableContainer>
                        <Table className={classes.table}>
                            <TableHead>
                                <TableRow className={classes.tableHead}>
                                    <TableCell className={classes.tableHead} align="center">Vehicle</TableCell>
                                    <TableCell className={classes.tableHead} align="center">Service</TableCell>
                                    <TableCell className={classes.tableHead} align="center">Date</TableCell>
                                    <TableCell className={classes.tableHead} align="center">Time</TableCell>
                                    <TableCell className={classes.tableHead} align="center">Comments</TableCell>
                                    <TableCell className={classes.tableHead} align="center"></TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {
                                    bookings.map(booking =>
                                        <TableRow key={booking.id}>
                                            <TableCell align="center" >{booking.vehicle.make + " " + booking.vehicle.model}</TableCell>
                                            <TableCell align="center" >{booking.service.description}</TableCell>
                                            <TableCell align="center">{moment(booking.date).format("DD/MMM/YYYY")}</TableCell>
                                            <TableCell align="center">{moment(booking.time, "HH:mm:ss").format("HH:mm")}</TableCell>
                                            <TableCell align="center">{booking.comments}</TableCell>
                                            <TableCell aligh="center" style={{ textAlign: 'center' }}>
                                                <Button
                                                    variant="contained"
                                                    color="secondary"
                                                    onClick={() => handleClickCancelBooking(booking.id)}
                                                    startIcon={<DeleteIcon />}
                                                    size="small"
                                                >
                                                    Cancel
                                            </Button>
                                            </TableCell>
                                        </TableRow>
                                    )
                                }
                            </TableBody>
                        </Table>
                    </TableContainer>
            }
        </div>
    )
}