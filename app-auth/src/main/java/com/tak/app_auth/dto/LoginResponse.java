package com.tak.app_auth.dto;

import lombok.*;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {}
