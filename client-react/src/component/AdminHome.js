import { AppBar, Box, Tab, Tabs, Typography } from '@material-ui/core'
import { useTheme } from '@material-ui/core/styles'
import React, { useState } from 'react'
import SwipeableViews from 'react-swipeable-views'
import { ToastContainer } from "react-toastify"
import AccountManagement from './AccountManagement'

export default function AdminHome() {

    const [tab, setTab] = useState(0)

    const theme = useTheme()

    const handleClickChangeTab = (event, newTab) => {
        event.preventDefault()
        setTab(newTab)
    }

    const handleChangeIndex = (index) => {
        setTab(index)
    }

    return(
        <div>
            <ToastContainer />
            <AppBar position="static" color="default">
                <Tabs
                    value={tab}
                    onChange={handleClickChangeTab}
                    indicatorColor="primary"
                    textColor="primary"
                    variant="fullWidth"
                >
                    <Tab label="Account management"></Tab>
                    <Tab label="Bookings"></Tab>
                    <Tab label="Invoices"></Tab>
                </Tabs>
            </AppBar>
            <SwipeableViews
                axis={theme.direction === 'rtl' ? 'x-reverse' : 'x'}
                index={tab}
                onChangeIndex={handleChangeIndex}
            >
                <TabPanel value={tab} index={0} id="tab-0" aria-controls="tabpanel-0" dir={theme.direction}>
                    <AccountManagement />
                </TabPanel>
                <TabPanel value={tab} index={1} id="tab-1" aria-controls="tabpanel-1" dir={theme.direction}>
                    tab 2
                </TabPanel>
                <TabPanel value={tab} index={2} id="tab-2" aria-controls="tabpanel-2" dir={theme.direction}>
                    tab 3
                </TabPanel>
            </SwipeableViews>
        </div>
    )
}

const TabPanel = ({ value, index, children }) => {
    return(
        <div
            role="tabpanel"
            hidden={value !== index}
            id={`tabpanel-${index}`}
            aria-labelledby={`tab-${index}`}
        >
            {value === index && (
                <Box p={3}>
                    <Typography component={'div'}>
                        {children}
                    </Typography>
                </Box>
            )}
        </div>
    )
}