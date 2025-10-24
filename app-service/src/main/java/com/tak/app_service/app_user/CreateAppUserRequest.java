package com.tak.app_service.app_user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateAppUserRequest {
    private String email;
    private String passwordHash;
    private AppUser.Role role;
}
