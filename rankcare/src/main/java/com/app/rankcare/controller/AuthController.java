package com.app.rankcare.controller;

import com.app.rankcare.payload.*;
import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import com.app.rankcare.exceptions.AppException;
import com.app.rankcare.model.Role;
import com.app.rankcare.model.RoleName;
import com.app.rankcare.model.User;
import com.app.rankcare.repository.RoleRepository;
import com.app.rankcare.repository.UserRepository;
import com.app.rankcare.security.JwtTokenProvider;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<Object>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<Object>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword(), signUpRequest.getPhoneNumber(),
                signUpRequest.getOrganization(), signUpRequest.getDesignation());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (signUpRequest.isAdmin() == null || !signUpRequest.isAdmin()) {
            sendWelcomeEmail(signUpRequest.getEmail(), signUpRequest.getUsername(), signUpRequest.getPassword());
        }

        Role userRole = roleRepository.findByName(signUpRequest.isAdmin() != null && signUpRequest.isAdmin() ? RoleName.ROLE_ADMIN : RoleName.ROLE_CLIENT)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    @PostMapping("/request-access")
    public ResponseEntity<?> requestAccess(@Valid @RequestBody AccessRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<Object>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            if (user.isAdmin()) {
                sendRequestEmail(user.getEmail(), signUpRequest);
            }
        }

        return new ResponseEntity<Object>(new ApiResponse(true, "Access requested successfully"), HttpStatus.OK);
    }

    private void sendRequestEmail(String email, AccessRequest signUpRequest) {
        Email from = new Email("no-reply@rankcare.com");
        String subject = "New User Request";
        Email to = new Email(email);

        String contentBuilder = "User details" + "\n\n" + "Name : " + signUpRequest.getName() + "\n\nEmail : " + signUpRequest.getEmail() + "\n\nPhone Number : " + signUpRequest.getPhoneNumber() + "\n\nOrganization : " + signUpRequest.getOrganization() + "\n\nDesignation : " + signUpRequest.getDesignation();

        Content content = new Content("text/plain", contentBuilder);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendWelcomeEmail(String email, String username, String password) {
        Email from = new Email("no-reply@rankcare.com");
        String subject = "Welcome to RankCare";
        Email to = new Email(email);

        String contentBuilder = "Below are your login details" + "\n\n" + "Username : " + username + "\n\nPassword : " + password;

        Content content = new Content("text/plain", contentBuilder);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}