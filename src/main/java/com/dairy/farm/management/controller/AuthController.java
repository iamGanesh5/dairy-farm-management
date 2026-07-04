package com.dairy.farm.management.controller;

import com.dairy.farm.management.dto.LoginRequest;

import com.dairy.farm.management.dto.LoginResponse;

import com.dairy.farm.management.entity.User;

import com.dairy.farm.management.repository.UserRepository;

import com.dairy.farm.management.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final UserRepository
            userRepository;

    private final PasswordEncoder
            passwordEncoder;

    private final JwtService
            jwtService;

    /*
     * LOGIN API
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(

            @RequestBody
            LoginRequest request

    ) {

        User user = userRepository
                .findByUsername(
                        request.getUsername()
                )
                .orElse(null);

        if (

                user == null ||

                        !passwordEncoder.matches(

                                request.getPassword(),

                                user.getPassword()

                        )

        ) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid Username or Password");

        }

        String token =
                jwtService.generateToken(
                        user.getUsername()
                );

        return ResponseEntity.ok(

                new LoginResponse(token)

        );

    }

    /*
     * REGISTER API
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(

            @RequestBody
            User request

    ) {

        /*
         * Check Username Exists
         */
        if (

                userRepository
                        .findByUsername(
                                request.getUsername()
                        )
                        .isPresent()

        ) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            "Username Already Exists"
                    );

        }

        /*
         * Create User
         */
        User user = User.builder()

                .username(
                        request.getUsername()
                )

                .password(

                        passwordEncoder.encode(
                                request.getPassword()
                        )

                )

                .role(
                        request.getRole()
                )

                .build();

        /*
         * Save User
         */
        userRepository.save(user);

        return ResponseEntity.ok(

                "User Registered Successfully"

        );

    }

}