package com.wellybean.gersgarage.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import com.wellybean.gersgarage.model.ERole;
import com.wellybean.gersgarage.model.Role;
import com.wellybean.gersgarage.model.User;
import com.wellybean.gersgarage.payload.request.SignUpRequest;
import com.wellybean.gersgarage.payload.request.LoginRequest;
import com.wellybean.gersgarage.payload.response.JwtResponse;
import com.wellybean.gersgarage.payload.response.MessageResponse;
import com.wellybean.gersgarage.repository.RoleRepository;
import com.wellybean.gersgarage.repository.UserRepository;
import com.wellybean.gersgarage.security.jwt.JwtUtils;
import com.wellybean.gersgarage.security.service.UserDetailsImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Authorization controller
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger LOGGER = LogManager.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    /**
     * Constructs instance of authorization controller
     * @param authenticationManager  authentication manager
     * @param userRepository  repository for users
     * @param roleRepository  repository for roles
     * @param encoder  password encoder
     * @param jwtUtils  token utilities, such as token generation and validation
     */
    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder encoder,
                          JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Exposes endpoint for signing in a user
     * @param loginRequest  login request
     * @return logged user information
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        LOGGER.info("[AUTH-CONTROL] - Going to authenticate user with username {}.", loginRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        LOGGER.info("[AUTH-CONTROL] - User {} authenticated successfully.", loginRequest.getUsername());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        LOGGER.info("[AUTH-CONTROL] - New token {} for user {}.", jwt, loginRequest.getUsername());

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

        LOGGER.info("[AUTH-CONTROL] - User {} has roles: {}.", loginRequest.getUsername(), roles);

        return ResponseEntity.ok(new JwtResponse(jwt,
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	
    }

    /**
     * Exposes endpoint for signing up a user
     * @param signUpRequest  sign up request
     * @return message response with either error or success message
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        
        User user = new User(
            signUpRequest.getUsername(),
            signUpRequest.getFirstName(),
            signUpRequest.getLastName(),
            signUpRequest.getEmail(),
            signUpRequest.getPhoneNumber(),
            encoder.encode(signUpRequest.getPassword())
        );

        List<String> strRoles = signUpRequest.getRoles();
        List<Role> roles = new ArrayList<>();

        if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
            // This is so only administrators can create an account of type "admin" and "mechanic"
            Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = loggedInUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
            if(!isAdmin) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    case "mechanic" -> {
                        Role mechanicRole = roleRepository.findByName(ERole.ROLE_MECHANIC)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(mechanicRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });  
        }
        user.setRoles(roles);
		userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/validateToken")
    public ResponseEntity<?> validateToken() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();

        return new ResponseEntity<>(String.format("Token valid for user %s.", loggedInUser.getName()), HttpStatus.OK);
    }

}
