package com.app.rankcare.controller;

import java.util.Optional;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.rankcare.model.User;
import com.app.rankcare.payload.ApiResponse;
import com.app.rankcare.payload.SignUpRequest;
import com.app.rankcare.payload.UserIdentityAvailability;
import com.app.rankcare.payload.UserSummary;
import com.app.rankcare.repository.UserRepository;
import com.app.rankcare.security.CurrentUser;
import com.app.rankcare.security.UserPrincipal;
import com.app.rankcare.repository.RoleRepository;
import com.app.rankcare.exceptions.AppException;
import com.app.rankcare.model.Role;
import com.app.rankcare.model.RoleName;

import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        logger.debug("Current user is null? " + currentUser.getName());
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName(),
                currentUser.isAdmin());
        return userSummary;
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) throws Exception {
        Optional<User> userOpt = userRepository.findById(id.longValue());
        if (!userOpt.isPresent()) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(userOpt.get(), HttpStatus.OK);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() throws Exception {
        List<User> users = userRepository.findAll();
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @PostMapping("/user/update")
    @PreAuthorize("hasRole('ADMIN')")
    public UserIdentityAvailability updateCurrentUser(@RequestBody SignUpRequest signUpRequest) {
        Optional<User> userOpt = userRepository.findById(signUpRequest.getId());
        if (!userOpt.isPresent()) {
            return new UserIdentityAvailability(false);
        }

        User user = userOpt.get();
        user.setName(signUpRequest.getName());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setOrganization(signUpRequest.getOrganization());
        user.setDesignation(signUpRequest.getDesignation());

        if(signUpRequest.getPassword() != null && !signUpRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        }

        userRepository.save(user);

        return new UserIdentityAvailability(true);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteById(@PathVariable("id") Integer id) throws Exception {
        userRepository.deleteById(id.longValue());
        return new ResponseEntity<Object>(new ApiResponse(true, "User deleted successfully!"), HttpStatus.OK);
    }
}