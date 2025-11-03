package com.tak.app_auth.dto;

import com.tak.app_auth.appUser.AppUser;
import lombok.*;


public record SignupRequest (
    String email,
    String passwordRow,
    AppUser.Role role
){}
