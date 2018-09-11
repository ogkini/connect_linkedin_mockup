package com.ted.controller;

import com.ted.model.User;
import com.ted.model.Role;
import com.ted.model.RoleName;
import com.ted.repository.UserRepository;
import com.ted.repository.RoleRepository;
import com.ted.exception.UserExistsException;
import com.ted.exception.AppException;
import com.ted.exception.BadRequestException;
import com.ted.request.SignInRequest;
import com.ted.request.SignUpRequest;
import com.ted.response.ApiResponse;
import com.ted.response.SignInResponse;
import com.ted.security.CurrentUser;
import com.ted.security.UserDetailsImpl;
import com.ted.service.UserService;
import com.ted.security.JwtTokenProvider;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    // Creates a new user
    @PostMapping("/users")
    @ResponseBody
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        // Check if the user already exists
        userRepository.findByEmail(signUpRequest.getEmail())
                .ifPresent((s) -> { throw new UserExistsException("A user with the same email already exists."); });

        // Create a User object from the request
        User user = new User(
                signUpRequest.getFirstname(),
                signUpRequest.getLastname(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword(),
                signUpRequest.getPicture()
        );

        // Encrypt the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign a user role
        Role role = roleRepository.findByName(RoleName.ROLE_USER);
        if (role == null) {
            throw new AppException("User Role not set.");
        }
        user.setRole(role);

        User result = userRepository.save(user);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(uri).body(new ApiResponse(true, "User created successfully."));
    }

    // Returns all registered users.
    // Only the admin can perform this action.
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAll() {
        return userService.getAll();
    }

    // Signs a user in to the app
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        // Check if the user exists
        User user = userRepository.findByEmail(signInRequest.getEmail()).orElse(null);
        if (user == null) {
            throw new BadRequestException("Invalid email or password.");
        }

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                signInRequest.getEmail(),
                signInRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication, user.getRole().getName().name());

        return ResponseEntity.ok(
            new SignInResponse(
                jwt,
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                user.getRole().getName().name()
            )
        );
    }

    // @PutMapping("/users/{userId}")
    // @PreAuthorize("hasRole('USER')")
    // public User updateUserById(@PathVariable(value = "userId") Long userId,
    //                            @Valid @RequestBody SignUpRequest userRequest,
    //                            @Valid @CurrentUser UserDetailsImpl currentUser) {
    //     return userService.updateUserById(userId, userRequest, currentUser);
    // }

}
