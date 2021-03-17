package com.wellybean.gersgarage.controller;

import com.wellybean.gersgarage.dto.VehicleDTO;
import com.wellybean.gersgarage.model.User;
import com.wellybean.gersgarage.model.Vehicle;
import com.wellybean.gersgarage.security.service.UserDetailsImpl;
import com.wellybean.gersgarage.service.UserService;
import com.wellybean.gersgarage.service.VehicleService;
import com.wellybean.gersgarage.util.EntityFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.wellybean.gersgarage.model.ERole.ROLE_USER;
import static com.wellybean.gersgarage.util.Constants.*;
import static com.wellybean.gersgarage.util.Constants.VALID_PASSWORD;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VehicleControllerTest {

    @InjectMocks
    VehicleController vehicleController;
    @Mock
    VehicleService vehicleService;
    @Mock
    UserService userService;
    @Mock
    private SecurityContext securityContext;

    @Before
    public void setup() {
        // Authenticated user details
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER.name()));
        UserDetailsImpl userDetails = new UserDetailsImpl(VALID_ID, VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        // Security context
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    public void registerVehicle_fails_userServiceThrowsException() {
        Vehicle newVehicle = new Vehicle();
        when(userService.findById(VALID_ID)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> vehicleController.registerVehicle(newVehicle));
        verify(userService).findById(VALID_ID);
    }

    @Test
    public void registerVehicle_fails_userServiceReturnsEmptyOptional() {
        Vehicle newVehicle = new Vehicle();
        when(userService.findById(VALID_ID)).thenReturn(Optional.empty());

        ResponseEntity<VehicleDTO> result = vehicleController.registerVehicle(newVehicle);

        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), result);
        verify(userService).findById(VALID_ID);
    }

    @Test
    public void registerVehicle_fails_userServiceReturnsValidUser_vehicleServiceThrowsException() {
        Vehicle newVehicle = new Vehicle();
        when(userService.findById(VALID_ID)).thenReturn(Optional.of(new User()));
        when(vehicleService.registerVehicle(newVehicle)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> vehicleController.registerVehicle(newVehicle));
        verify(userService).findById(VALID_ID);
        verify(vehicleService).registerVehicle(newVehicle);
    }

    @Test
    public void registerVehicle_succeeds_userServiceReturnsValidUser_vehicleServiceSucceeds() {
        // Method input
        Vehicle newVehicle = new Vehicle();
        // Returned from user service
        User user = EntityFactory.getRegularUser();
        when(userService.findById(VALID_ID)).thenReturn(Optional.of(user));
        // Returned from vehicle service
        Vehicle savedVehicle = new Vehicle();
        savedVehicle.setUser(user);
        VehicleDTO savedVehicleDTO = new VehicleDTO(savedVehicle);
        when(vehicleService.registerVehicle(newVehicle)).thenReturn(savedVehicle);

        ResponseEntity<VehicleDTO> result = vehicleController.registerVehicle(newVehicle);

        assertEquals(new ResponseEntity<>(savedVehicleDTO, HttpStatus.CREATED), result);
        verify(userService).findById(VALID_ID);
        verify(vehicleService).registerVehicle(newVehicle);
    }

    @Test
    public void getVehiclesForUser_fails_vehiclesServiceThrowsException() {
        when(vehicleService.getVehiclesForUser(VALID_USERNAME)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> vehicleController.getVehiclesForUser());
        verify(vehicleService).getVehiclesForUser(VALID_USERNAME);
    }

    @Test
    public void getVehiclesForUser_fails_vehiclesServiceReturnsEmptyOptional() {
        when(vehicleService.getVehiclesForUser(VALID_USERNAME)).thenReturn(Optional.empty());

        ResponseEntity<?> result = vehicleController.getVehiclesForUser();

        assertEquals(new ResponseEntity<>(HttpStatus.NO_CONTENT), result);
        verify(vehicleService).getVehiclesForUser(VALID_USERNAME);
    }

    @Test
    public void getVehiclesForUser_succeeds_vehiclesServiceReturnsListOfVehicles() {
        List<Vehicle> vehicleList = EntityFactory.getVehicleListForUser(10);
        List<VehicleDTO> vehicleDTOList = vehicleList.stream().map(VehicleDTO::new).collect(Collectors.toList());
        when(vehicleService.getVehiclesForUser(VALID_USERNAME)).thenReturn(Optional.of(vehicleList));

        ResponseEntity<?> result = vehicleController.getVehiclesForUser();

        assertEquals(new ResponseEntity<>(vehicleDTOList, HttpStatus.OK), result);
        verify(vehicleService).getVehiclesForUser(VALID_USERNAME);
    }

    @Test
    public void deleteVehicle_fails_vehicleServiceThrowsException() {
        when(vehicleService.getVehicle(VALID_ID)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> vehicleController.deleteVehicle(VALID_ID));
        verify(vehicleService).getVehicle(VALID_ID);
    }

    @Test
    public void deleteVehicle_fails_vehicleServiceReturnsEmptyOptional() {
        when(vehicleService.getVehicle(VALID_ID)).thenReturn(Optional.empty());

        vehicleController.deleteVehicle(VALID_ID);
        verify(vehicleService).getVehicle(VALID_ID);
    }

    @Test
    public void deleteVehicle_fails_vehicleReturnsValidVehicle_usernamesMismatch() {
        Vehicle vehicle = EntityFactory.getVehicle();
        vehicle.getUser().setUsername("anotherUserName");
        when(vehicleService.getVehicle(VALID_ID)).thenReturn(Optional.of(vehicle));

        ResponseEntity<?> result = vehicleController.deleteVehicle(VALID_ID);

        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), result);
        verify(vehicleService).getVehicle(VALID_ID);
    }

    @Test
    public void deleteVehicle_fails_vehicleReturnsValidVehicle_vehicleServiceThrowsExceptionWhenDeleting() {
        Vehicle vehicle = EntityFactory.getVehicle();
        when(vehicleService.getVehicle(VALID_ID)).thenReturn(Optional.of(vehicle));
        doThrow(RuntimeException.class).when(vehicleService).deleteVehicle(vehicle);

        assertThrows(RuntimeException.class, () -> vehicleController.deleteVehicle(VALID_ID));

        verify(vehicleService).getVehicle(VALID_ID);
        verify(vehicleService).deleteVehicle(vehicle);
    }

    @Test
    public void deleteVehicle_succeeds() {
        Vehicle vehicle = EntityFactory.getVehicle();
        when(vehicleService.getVehicle(VALID_ID)).thenReturn(Optional.of(vehicle));

        ResponseEntity<?> result = vehicleController.deleteVehicle(VALID_ID);

        assertEquals(new ResponseEntity<>(HttpStatus.NO_CONTENT), result);
        verify(vehicleService).getVehicle(VALID_ID);
        verify(vehicleService).deleteVehicle(vehicle);
    }
}