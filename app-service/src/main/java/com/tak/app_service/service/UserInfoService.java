package com.tak.app_service.service;

import com.tak.app_service.dto.user.UserInfo;
import com.tak.app_service.repository.AppUserRepository;
import com.tak.app_service.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final AppUserRepository appUserRepository;
    private final MeetingRepository meetingRepository;

    public UserInfo getUserProfile(Long userId) {
        UserInfo userInfo = new UserInfo(

        );
        return null;
    }
}
