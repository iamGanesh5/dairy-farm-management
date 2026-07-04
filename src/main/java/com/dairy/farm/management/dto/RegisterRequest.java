package com.dairy.farm.management.dto;

import com.dairy.farm.management.enums.Role;

import lombok.Data;

@Data
public class RegisterRequest {

    private String username;

    private String password;

    private Role role;

}