package com.tak.app_auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoginResponse {
    String accessToken;
    String refreshToken;
}
