package com.tak.app_service.service;

import com.tak.app_service.dto.user.UserInfo;
import com.tak.app_service.repository.AppUserRepository;
import com.tak.app_service.repository.MeetingMemberRepository;
import com.tak.common.appUser.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final AppUserRepository appUserRepository;
    private final MeetingMemberRepository meetingMemberRepository;

    public UserInfo getUserProfile(Long userId) {
        var userOpt = appUserRepository.findById(userId);
        if(userOpt.isEmpty()){
            // need to feat error handling
            return null;
        }
        var user = userOpt.get();
        Integer meetingParticipationCount = meetingMemberRepository.countByUserId(userId);
        return UserInfo.from(user, meetingParticipationCount);
    }

    public UserInfo updateUserProfile(Long userId, String nickname, String department, String bio, Long profileImageId) {
        var userOpt = appUserRepository.findById(userId);
        if(userOpt.isEmpty()){
            // need to feat error handling
            return null;
        }
        var user = userOpt.get();
        if(nickname != null && !nickname.isEmpty()){
            user.setNickname(nickname);
        }
        if(department!= null && !department.isEmpty()){
            user.setDepartment(department);
        }
        if(bio != null && !bio.isEmpty()){
            user.setBio(bio);
        }
        if(profileImageId!=null){
            user.setProfileImageId(profileImageId);
        }
        AppUser save = appUserRepository.save(user);
        Integer meetingParticipationCount = meetingMemberRepository.countByUserId(userId);
        return UserInfo.from(save, meetingParticipationCount);
    }
}
