package com.tak.app_auth.dto;

import com.tak.app_auth.appUser.AppUser;


public record SignupRequest (
    String email,
    String passwordRow,
    String nickname,
    AppUser.Role role
){}
