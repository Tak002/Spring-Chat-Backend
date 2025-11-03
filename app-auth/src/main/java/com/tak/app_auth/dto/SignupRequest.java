package com.tak.app_auth.dto;

import com.tak.common.appUser.AppUser;

import java.time.LocalDate;


public record SignupRequest (
    String email,
    String passwordRow,
    String nickname,
    String department,
    String bio,
    LocalDate birthDate,
    String certificationNumber,
    AppUser.Role role
){}
