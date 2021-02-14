package com.wellybean.gersgarage.controller;

import java.util.Collections;
import com.wellybean.gersgarage.payload.request.LoginRequest;
import com.wellybean.gersgarage.payload.response.JwtResponse;
import com.wellybean.gersgarage.repository.RoleRepository;
import com.wellybean.gersgarage.repository.UserRepository;
import com.wellybean.gersgarage.security.jwt.JwtUtils;
import com.wellybean.gersgarage.security.service.UserDetailsImpl;
import static com.wellybean.gersgarage.model.ERole.ROLE_USER;
import static com.wellybean.gersgarage.util.Constants.*;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private JwtUtils jwtUtils;

    @Test
    public void authenticateUser_Succeeds() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(VALID_USERNAME);
        loginRequest.setPassword(VALID_PASSWORD);

        UserDetailsImpl userDetails = new UserDetailsImpl(VALID_ID, VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD,
                Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER.name())));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(VALID_TOKEN);

        ResponseEntity<?> actual = authController.authenticateUser(loginRequest);
        ResponseEntity<?> expected = ResponseEntity.ok(new JwtResponse(VALID_TOKEN, VALID_ID, VALID_USERNAME, VALID_EMAIL,
                Collections.singletonList(ROLE_USER.name())));

        assertEquals(expected, actual);
    }

    @Ignore
    @Test
    public void registerUser() {
    }
}