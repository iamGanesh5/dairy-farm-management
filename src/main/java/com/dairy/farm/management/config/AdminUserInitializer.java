package com.dairy.farm.management.config;

import com.dairy.farm.management.entity.User;

import com.dairy.farm.management.enums.Role;

import com.dairy.farm.management.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer
        implements CommandLineRunner {

    private final UserRepository
            userRepository;

    private final PasswordEncoder
            passwordEncoder;

    @Override
    public void run(String... args)
            throws Exception {

        if (
                userRepository
                        .findByUsername("admin")
                        .isEmpty()
        ) {

            User admin = User.builder()

                    .username("admin")

                    .password(

                            passwordEncoder.encode(
                                    "admin123"
                            )

                    )

                    .role(Role.ADMIN)

                    .build();

            userRepository.save(admin);

            System.out.println(
                    "Admin User Created"
            );

        }

    }

}