import React, { useEffect, useState } from 'react'
import { Table, TableBody, TableContainer, TablePagination, TableHead, TableRow, TableCell, TableFooter, Button, Modal, FormControl, InputLabel, OutlinedInput, Select, MenuItem } from "@material-ui/core"
import { withStyles, makeStyles } from '@material-ui/core/styles';
import userService from '../service/UserService'
import notificationService from '../service/NotificationService'
import DeleteIcon from '@material-ui/icons/Delete'
import Paper from '@material-ui/core/Paper';

const StyledTableCell = withStyles((theme) => ({
    head: {
      backgroundColor: theme.palette.common.black,
      color: theme.palette.common.white,
    },
    body: {
      fontSize: 14,
    },
  }))(TableCell);
  
  const StyledTableRow = withStyles((theme) => ({
    root: {
      '&:nth-of-type(odd)': {
        backgroundColor: theme.palette.action.hover,
      },
    },
  }))(TableRow);

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
    }, panel: {
        textAlign: "center"
    }
}));

export default function AccountManagement() {

    const classes = useStyles()
    const [users, setUsers] = useState([])

    // table
    const [page, setPage] = useState(0)
    const [rowsPerPage, setRowsPerPage] = useState(10)

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

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    }

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    }

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
            <TableContainer component={Paper}>
                    <Table className={classes.table}>
                        <TableHead>
                            <StyledTableRow className={classes.tableHead}>
                                <StyledTableCell className={classes.tableHead} align="center">ID</StyledTableCell>
                                <StyledTableCell className={classes.tableHead} align="center">Username</StyledTableCell>
                                <StyledTableCell className={classes.tableHead} align="center">Name</StyledTableCell>
                                <StyledTableCell className={classes.tableHead} align="center">Email</StyledTableCell>
                                <StyledTableCell className={classes.tableHead} align="center">Phone Number</StyledTableCell>
                                <StyledTableCell className={classes.tableHead} align="center">Roles</StyledTableCell>
                                <StyledTableCell className={classes.tableHead} align="center"></StyledTableCell>
                                {/* <StyledTableCell className={classes.tableHead} align="center"></StyledTableCell> */}
                            </StyledTableRow>
                        </TableHead>
                        <TableBody>
                            {
                                users.map(user => 
                                    <StyledTableRow key={user.id}>
                                        <StyledTableCell align="center" >{user.id}</StyledTableCell>
                                        <StyledTableCell align="center" >{user.username}</StyledTableCell>
                                        <StyledTableCell align="center" >{user.fullName}</StyledTableCell>
                                        <StyledTableCell align="center">{user.email}</StyledTableCell>
                                        <StyledTableCell align="center">{user.phoneNumber}</StyledTableCell>
                                        <StyledTableCell align="center">{user.roles.map(role => <p>{role}</p>)}</StyledTableCell>
                                        <StyledTableCell aligh="center" style={{textAlign:'center'}}>
                                            <Button 
                                                variant="contained" 
                                                color="secondary" 
                                                onClick={() => handleClickDeleteUser(user.id)} 
                                                startIcon={<DeleteIcon />}
                                                size="small"
                                            >
                                                Remove
                                            </Button>
                                        </StyledTableCell>
                                    </StyledTableRow>
                                )
                            }
                        </TableBody>
                        <TableFooter>
                            <TableRow>
                                <TablePagination 
                                    rowsPerPageOptions={[10, 50]} 
                                    count={users.length}
                                    rowsPerPage={rowsPerPage}
                                    page={page}
                                    onChangePage={handleChangePage}
                                    onChangeRowsPerPage={handleChangeRowsPerPage}
                                />
                            </TableRow>
                        </TableFooter>
                    </Table>
                </TableContainer>
        </div>
    )
}