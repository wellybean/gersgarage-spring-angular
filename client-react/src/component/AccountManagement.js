import React, { useEffect, useState } from 'react'
import { Table, TableBody, TableContainer, TableHead, TableRow, TableCell, Typography, Button, Modal, FormControl, InputLabel, OutlinedInput, Select, MenuItem } from "@material-ui/core"
import { makeStyles } from '@material-ui/core/styles';
import userService from '../service/UserService'
import notificationService from '../service/NotificationService'
import DeleteIcon from '@material-ui/icons/Delete'

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
    }, panel: {
        textAlign: "center"
    }
}));

export default function AccountManagement() {

    const classes = useStyles()
    const [users, setUsers] = useState([])

    useEffect(() => {
        userService
            .getUsers()
            .then(
                response => {
                    setUsers(response)
                },
                error => {
                    console.error(error)
                }
            )
    })

    const handleClickDeleteUser = id => {
        userService
            .deleteUserAccount(id)
            .then(
                () => {
                    setUsers(users.filter(user => user.id !== id))
                    notificationService.success("User account deleted successfully.")
                },
                error => notificationService.error(`Failed to delete user account due to: ${error}`)
            )
    }

    return(
        <div>
            <TableContainer>
                    <Table className={classes.table}>
                        <TableHead>
                            <TableRow className={classes.tableHead}>
                                <TableCell className={classes.tableHead} align="center">ID</TableCell>
                                <TableCell className={classes.tableHead} align="center">Username</TableCell>
                                <TableCell className={classes.tableHead} align="center">Name</TableCell>
                                <TableCell className={classes.tableHead} align="center">Email</TableCell>
                                <TableCell className={classes.tableHead} align="center">Phone Number</TableCell>
                                <TableCell className={classes.tableHead} align="center">Roles</TableCell>
                                <TableCell className={classes.tableHead} align="center"></TableCell>
                                <TableCell className={classes.tableHead} align="center"></TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {
                                users.map(user => 
                                    <TableRow key={user.id}>
                                        <TableCell align="center" >{user.id}</TableCell>
                                        <TableCell align="center" >{user.username}</TableCell>
                                        <TableCell align="center" >{user.fullName}</TableCell>
                                        <TableCell align="center">{user.email}</TableCell>
                                        <TableCell align="center">{user.phoneNumber}</TableCell>
                                        <TableCell align="center">{user.roles.map(role => <p>{role}</p>)}</TableCell>
                                        <TableCell aligh="center" style={{textAlign:'center'}}>
                                            <Button 
                                                variant="contained" 
                                                color="secondary" 
                                                onClick={() => handleClickDeleteUser(user.id)} 
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
        </div>
    )
}