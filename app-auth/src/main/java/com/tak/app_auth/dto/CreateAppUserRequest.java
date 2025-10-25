package com.tak.app_auth.dto;

import com.tak.app_auth.appUser.AppUser;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateAppUserRequest {
    private String email;
    private String passwordRow;
    private AppUser.Role role;
}
