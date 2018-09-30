package com.ted.controller;

import com.ted.exception.AppException;
import com.ted.exception.BadRequestException;
import com.ted.exception.NotAuthorizedException;
import com.ted.exception.UserExistsException;
import com.ted.model.Role;
import com.ted.model.RoleName;
import com.ted.model.User;
import com.ted.repository.RoleRepository;
import com.ted.repository.UserRepository;
import com.ted.request.SignInRequest;
import com.ted.request.SignUpRequest;
import com.ted.request.UpdateRequest;
import com.ted.response.ApiResponse;
import com.ted.response.SignInResponse;
import com.ted.response.UpdateResponse;
import com.ted.security.CurrentUser;
import com.ted.security.JwtTokenProvider;
import com.ted.security.UserDetailsImpl;
import com.ted.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

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
    @Transactional
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
    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public User getById(@PathVariable(value = "userId") Long userId, @Valid @CurrentUser UserDetailsImpl currentUser) {
        return userService.getById(userId, currentUser);
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

    // Returns multiple users matching the search.
    @GetMapping("/users/searchUser")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<User> getUsersBySearchTerm(@RequestParam("searchTerm") String searchTerm) {
        // Check how the frontend handles the returnes list..
        // Generally we want to display "no results" when there was an actual search term,
        // if no searchTerm was given.. then display an error.
        if (searchTerm == null || searchTerm.isEmpty()) {
            logger.error("No searchTerm reached the backEnd!");
            return null;
        } else {
            searchTerm = StringUtils.lowerCase(searchTerm); // Match to more records.
        }

        List<User> users;
        String firstTerm;
        String secondTerm;

        // Get firstName and lastName from search string.
        String[] multipleTerms = StringUtils.split(searchTerm, ' ');

        if (multipleTerms.length == 0) {
            logger.warn("Term-splitting provided no results!");
            return null;
        } else if (multipleTerms.length == 1) {
            firstTerm = multipleTerms[0];
            users = userService.getBySearch(firstTerm, firstTerm);
            if (users == null || users.isEmpty()) {
                logger.warn("No users found for searchTerm: \"" + searchTerm + "\"!");
            }
        } else {
            if ( multipleTerms.length > 2 ) {
                logger.warn("More than 2 terms found in \"searchTerm\"! We will use only the first two!");
            }

            firstTerm = multipleTerms[0];
            secondTerm = multipleTerms[1];

            users = userService.getBySearch(firstTerm, secondTerm);

            // The underneath query handles the case where the both searchTerms can be either the firstName of the lastName of the user in the DB.
            // Example case: there might be "Kwnstantinos Andreou" and "Adnreas Kwnstantinou", and the user to search for: "Kwnst Andr")

            if (users == null || users.isEmpty()) {
                logger.warn("No users found for searchTerm: \"" + searchTerm + "\"!");
            }
        }

        return users;
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


    @PutMapping("/users/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateUserInfo(@PathVariable(value = "userId") Long userId,
                                            @Valid @RequestBody UpdateRequest updateRequest) {

        User user = updateRequest.asUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));   // Encrypt the password

        //logger.debug(user.basicInfoToString());

        int affectedRows = this.userService.updateUserData(user);
        if ( affectedRows == 1 ) {
            logger.debug("User info was updated for user with id: " + userId);
            return ResponseEntity.ok(
                    new UpdateResponse(updateRequest.asUser())
            );
        }
        else {
            logger.warn("No user was found in DataBase having userId: " + userId);
            return ResponseEntity.badRequest().build();
        }

    }

}
