package com.ted.controller;

import com.ted.model.Role;
import com.ted.model.RoleName;
import com.ted.model.User;
import com.ted.repository.RoleRepository;
import com.ted.repository.UserRepository;
import com.ted.request.SignInRequest;
import com.ted.request.SignUpRequest;
import com.ted.response.ApiResponse;
import com.ted.response.SignInResponse;
import com.ted.exception.AppException;
import com.ted.exception.BadRequestException;
import com.ted.exception.UserExistsException;
import com.ted.exception.NotAuthorizedException;
import com.ted.service.UserService;
import com.ted.security.JwtTokenProvider;
import com.ted.security.CurrentUser;
import com.ted.security.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;

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
                signUpRequest.getPicture()  // It may be null since it's optional, no problem.
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

    // Returns a specific user.
    // Only the admin or the user himself can perform this action.
    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public User getById(@PathVariable(value = "userId") Long userId, @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if (!currentUser.getId().equals(userId)
            && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new NotAuthorizedException("You are not authorized to access this resource.");
        }

        return userService.getById(userId);
    }

    // Returns a specific user.
    // Only the admin or the user himself can perform this action.
    @GetMapping("/users/getUserByEmail")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public User getByEmail(@RequestParam("userEmail") String userEmail, @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if ( !currentUser.getEmail().equals(userEmail)
                && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new NotAuthorizedException("You are not authorized to access this resource.");
        }

        return userService.getByEmail(userEmail);
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
                user.getId(),
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                user.getRole().getName().name()
            )
        );
    }

}
