import { Table, TableBody, TableContainer, TableHead, TableRow, TableCell, Typography, Button, Modal, FormControl, InputLabel, OutlinedInput, Select, MenuItem } from "@material-ui/core"
import React, { useEffect, useState } from "react"
import DeleteIcon from '@material-ui/icons/Delete'
import { makeStyles } from '@material-ui/core/styles';
import vehicleService from "../service/VehicleService"

const useStyles = makeStyles((theme) =>({
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
        height: 500,
        border: '2px solid #000',
        boxShadow: theme.shadows[5],
        padding: theme.spacing(2, 4, 3),
    },
    textField: {
        marginTop: theme.spacing(1),
        width:"100%"
    },
    select: {
        marginTop: theme.spacing(1),
        width:"100%"
    }, tableHead: {
        fontWeight: "bold"
    }
}));

export default function Vehicles() {

    const[vehicles, setVehicles] = useState([])
    const[vehicleModalOpen, setVehicleOpenModal] = useState(false)
    const classes = useStyles()
    const [newVehicleInformation, setNewVehicleInformation] = useState({
        type: '',
        make: '',
        model: '',
        licensePlate: '',
        engine: ''
    })

    useEffect(() => {
        vehicleService.getVehiclesForUser().then(
            response => setVehicles(response)
        )
    }, [])

    const handleClickDeleteVehicle = id => {
        vehicleService
            .deleteVehicle(id)
            .then(() =>
                setVehicles(vehicles.filter(vehicle => vehicle.id !== id))
            ).catch(error =>
                console.log(error)
            )
    }

    const handleClickOpenVehicleModal = event => {
        event.preventDefault()
        setVehicleOpenModal(true)
    }

    const handleClickRegisterVehicle = event => {
        event.preventDefault()
        vehicleService
            .registerVehicle(newVehicleInformation)
            .then(newVehicle => 
                setVehicles(vehicles.concat(newVehicle))
            )
            .catch(error => console.log(error))
        setNewVehicleInformation({type: "", make: "", model: "", licensePlate: "", engine: ""})
    }

    const handleCloseVehicleModal = () => {
        setVehicleOpenModal(false)
    }

    const handleChange = (prop) => event => {
        setNewVehicleInformation({ ...newVehicleInformation, [prop]: event.target.value })
    }

    return(
        <div>
            <Button
                variant="contained" 
                color="primary" 
                onClick={handleClickOpenVehicleModal} 
                size="small"
            >
                Register vehicle
            </Button>
            <Modal
                open={vehicleModalOpen}
                onClose={handleCloseVehicleModal}
                aria-labelledby="simple-modal-title"
                aria-describedby="simple-modal-description"
            >
                <div className={classes.modal}>
                    <form>
                        <br />
                        <h2>Vehicle registration</h2>
                        <FormControl variant="outlined" className={classes.select}>
                            <InputLabel htmlFor="type">Vehicle Type</InputLabel>
                            <Select
                                id="type"
                                type=""
                                value={newVehicleInformation.type}
                                onChange={handleChange('type')}
                                labelWidth={100}
                            >
                                <MenuItem value="Car">Car</MenuItem>
                                <MenuItem value="Motorbike">Motorbike</MenuItem>
                                <MenuItem value="Van">Van</MenuItem>
                            </Select>
                        </FormControl>
                        <br />
                        <FormControl variant="outlined" className={classes.textField}>
                            <InputLabel htmlFor="make">Make</InputLabel>
                            <OutlinedInput 
                                id="make"
                                type="text"
                                value={newVehicleInformation.make}
                                onChange={handleChange('make')}
                                labelWidth={100}
                            />
                        </FormControl>
                        <br />
                        <FormControl variant="outlined" className={classes.textField}>
                            <InputLabel htmlFor="model">Model</InputLabel>
                            <OutlinedInput 
                                id="model"
                                type="text"
                                value={newVehicleInformation.model}
                                onChange={handleChange('model')}
                                labelWidth={100}
                            />
                        </FormControl>
                        <br />
                        <FormControl variant="outlined" className={classes.textField}>
                            <InputLabel htmlFor="licensePlate">License plate</InputLabel>
                            <OutlinedInput 
                                id="licensePlate"
                                type="text"
                                value={newVehicleInformation.licensePlate}
                                onChange={handleChange('licensePlate')}
                                labelWidth={100}
                            />
                        </FormControl>
                        <br />
                        <FormControl variant="outlined" className={classes.select}>
                            <InputLabel htmlFor="engine">Engine type</InputLabel>
                            <Select
                                id="engine"
                                type="select"
                                value={newVehicleInformation.engine}
                                onChange={handleChange('engine')}
                                labelWidth={100}
                            >
                                <MenuItem value="Petrol">Petrol</MenuItem>
                                <MenuItem value="Diesel">Diesel</MenuItem>
                                <MenuItem value="Hybrid">Hybrid</MenuItem>
                            </Select>
                        </FormControl>
                        <br />
                        <br />
                        <Button variant="contained" color="primary" className={classes.button} onClick={handleClickRegisterVehicle}>
                            Register
                        </Button>   
                    </form>
                </div>
            </Modal>
            <br />
            <br />
            {
                vehicles.length === 0 ?
                <Typography>No vehicles available</Typography> :
                <TableContainer>
                    <Table className={classes.table}>
                        <TableHead>
                            <TableRow className={classes.tableHead}>
                                <TableCell className={classes.tableHead} align="center">Vehicle type</TableCell>
                                <TableCell className={classes.tableHead} align="center">Make</TableCell>
                                <TableCell className={classes.tableHead} align="center">Model</TableCell>
                                <TableCell className={classes.tableHead} align="center">License Plate</TableCell>
                                <TableCell className={classes.tableHead} align="center">Engine</TableCell>
                                <TableCell className={classes.tableHead} align="center"></TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {
                                vehicles.map(vehicle => 
                                    <TableRow key={vehicle.id}>
                                        <TableCell align="center" >{vehicle.type}</TableCell>
                                        <TableCell align="center" >{vehicle.make}</TableCell>
                                        <TableCell align="center">{vehicle.model}</TableCell>
                                        <TableCell align="center">{vehicle.licensePlate}</TableCell>
                                        <TableCell align="center">{vehicle.engine}</TableCell>
                                        <TableCell aligh="center" style={{textAlign:'center'}}>
                                            <Button 
                                                variant="contained" 
                                                color="secondary" 
                                                onClick={() => handleClickDeleteVehicle(vehicle.id)} 
                                                startIcon={<DeleteIcon />}
                                                size="small"
                                            >
                                                Remove
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