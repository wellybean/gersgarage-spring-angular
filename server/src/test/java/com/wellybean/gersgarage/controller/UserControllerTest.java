package com.wellybean.gersgarage.controller;

import com.wellybean.gersgarage.dto.UserDTO;
import com.wellybean.gersgarage.model.Role;
import com.wellybean.gersgarage.model.User;
import com.wellybean.gersgarage.security.service.UserDetailsImpl;
import com.wellybean.gersgarage.service.UserService;
import com.wellybean.gersgarage.util.EntityFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
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

import java.util.*;
import java.util.stream.Collectors;

import static com.wellybean.gersgarage.model.ERole.*;
import static com.wellybean.gersgarage.util.Constants.*;
import static com.wellybean.gersgarage.util.Constants.VALID_PASSWORD;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    @Mock
    private SecurityContext securityContext;

    @Before
    public void prepare() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void test_getAllUsers_succeeds_authorizedUser() {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(ROLE_ADMIN.name()));

        UserDetailsImpl userDetails = new UserDetailsImpl(VALID_ID, VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        List<User> userList = Arrays.asList(
                EntityFactory.getUser(1L, EntityFactory.listWithAllRoles()),
                EntityFactory.getUser(2L, EntityFactory.listWithAllRoles()),
                EntityFactory.getUser(3L, EntityFactory.listWithAllRoles())
        );

        when(userService.findAll()).thenReturn(userList);

        List<UserDTO> userDTOList = userList.stream().map(UserDTO::new).collect(Collectors.toList());

        assertEquals(new ResponseEntity<>(userDTOList, HttpStatus.OK), userController.getAllUsers());
    }

    @Test
    public void  test_getAllUsers_fails_unauthorized() {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER.name()));

        UserDetailsImpl userDetails = new UserDetailsImpl(VALID_ID, VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        assertEquals(new ResponseEntity<>(HttpStatus.UNAUTHORIZED), userController.getAllUsers());
    }

    @Test
    public void test_deleteUserAccount_succeeds_adminUser() {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(ROLE_ADMIN.name()));

        UserDetailsImpl userDetails = new UserDetailsImpl(VALID_ID, VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        Role role = new Role();
        role.setName(ROLE_USER);

        User user = EntityFactory.getUser(2L,Collections.singletonList(role));

        when(userService.findById(2L)).thenReturn(Optional.of(user));

        assertEquals(new ResponseEntity<>(HttpStatus.NO_CONTENT), userController.deleteUserAccount(2L));
        verify(userService).deleteUser(user);
    }

    @Test
    public void test_deleteUserAccount_succeeds_sameUser() {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER.name()));

        UserDetailsImpl userDetails = new UserDetailsImpl(VALID_ID, VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        Role role = new Role();
        role.setName(ROLE_USER);

        User user = EntityFactory.getUser(Collections.singletonList(role));

        when(userService.findById(VALID_ID)).thenReturn(Optional.of(user));

        assertEquals(new ResponseEntity<>(HttpStatus.NO_CONTENT), userController.deleteUserAccount(VALID_ID));
        verify(userService).deleteUser(user);
    }

    @Test
    public void test_deleteUserAccount_fails_unauthorized() {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER.name()));

        UserDetailsImpl userDetails = new UserDetailsImpl(VALID_ID, VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        Role role = new Role();
        role.setName(ROLE_USER);

        User user = EntityFactory.getUser(Collections.singletonList(role));

        when(userService.findById(2L)).thenReturn(Optional.of(user));

        assertEquals(new ResponseEntity<>(HttpStatus.UNAUTHORIZED), userController.deleteUserAccount(2L));
        verify(userService, times(0)).deleteUser(user);
    }

    @Test
    public void test_deleteUserAccount_fails_badRequest() {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER.name()));

        UserDetailsImpl userDetails = new UserDetailsImpl(VALID_ID, VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(userService.findById(2L)).thenReturn(Optional.empty());

        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), userController.deleteUserAccount(2L));
    }
}