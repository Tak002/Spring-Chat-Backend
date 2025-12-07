package com.tak.app_service.service;

import com.tak.app_service.dto.user.UserInfo;
import com.tak.app_service.repository.AppUserRepository;
import com.tak.app_service.repository.MeetingMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final AppUserRepository appUserRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    public UserInfo getUserProfile(Long userId) {
        if(!appUserRepository.existsById(userId)){
            // need to feat error handling
            return null;
        }
        var user = appUserRepository.findById(userId).get();
        Integer meetingParticipationCount = meetingMemberRepository.countByUserId(userId);
        return UserInfo.from(user, meetingParticipationCount);
    }
}
