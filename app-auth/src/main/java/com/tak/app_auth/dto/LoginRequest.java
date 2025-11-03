package com.tak.app_auth.dto;

import lombok.*;

public record LoginRequest(
        String email,
        String passwordRow
) {}