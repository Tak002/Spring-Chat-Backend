package com.tak.app_service.service;

import com.tak.app_service.dto.meeting.MeetingCreateRequest;
import com.tak.app_service.dto.meeting.MeetingDetailDto;
import com.tak.app_service.dto.meeting.MeetingDto;
import com.tak.app_service.entity.Meeting;
import com.tak.app_service.entity.MeetingMember;
import com.tak.app_service.entity.enums.MeetingMemberState;
import com.tak.app_service.repository.AppUserRepository;
import com.tak.app_service.repository.MeetingMemberRepository;
import com.tak.app_service.repository.MeetingRepository;
import com.tak.common.appUser.AppUser;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    private final AppUserRepository appUserRepository;

    public MeetingDto createMeeting(MeetingCreateRequest meetingCreateRequest, Long hostId) {
        Integer minAge = meetingCreateRequest.rules() != null && meetingCreateRequest.rules().ageRange() != null
                ? meetingCreateRequest.rules().ageRange().min() : null;
        Integer maxAge = meetingCreateRequest.rules() != null && meetingCreateRequest.rules().ageRange() != null
                ? meetingCreateRequest.rules().ageRange().max() : null;

        Meeting meeting = Meeting.builder()
                .hostId(hostId)
                .title(meetingCreateRequest.title())
                .description(meetingCreateRequest.description())
                .startAt(meetingCreateRequest.startAt())
                .endAt(meetingCreateRequest.endAt())
                .place(meetingCreateRequest.place())
                .maxMembers(meetingCreateRequest.participantLimit())
                .thumbnailId(meetingCreateRequest.thumbnailId())
                .linkedEventId(meetingCreateRequest.linkedEventId() != null ? meetingCreateRequest.linkedEventId() : null)
                .gender(meetingCreateRequest.rules() != null ? meetingCreateRequest.rules().gender() : null)
                .minAge(minAge)
                .maxAge(maxAge)
                .build();

        Meeting newMeeting = meetingRepository.save(meeting);
        MeetingMember host = MeetingMember.builder()
                .meetingId(newMeeting.getId())
                .userId(hostId)
                .role(com.tak.app_service.entity.enums.MeetingMemberRole.HOST)
                .state(MeetingMemberState.APPROVED)
                .build();
        meetingMemberRepository.save(host);
        return MeetingDto.toDto(newMeeting);
    }
    public List<MeetingDto> getMeetings() {
        List<Meeting> all = meetingRepository.findAll();
        return all.stream().map(MeetingDto::toDto).collect(Collectors.toList());
    }

    public MeetingDetailDto getMeetingDetails(Long id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meeting not found with id: " + id));
        List<Long> memberIdList = meetingMemberRepository.findUserIdByMeetingId(id);
        List<AppUser> members = memberIdList.stream()
                .map(i -> appUserRepository.findById(i)
                        .orElseThrow(() -> new EntityNotFoundException("AppUser not found with id: " + i)))
                .toList();

        return MeetingDetailDto.from(meeting, members);
    }

    public List<MeetingDto> getMyMeetings(Long userId) {
        List<Long> meetingIdList = meetingMemberRepository.findMeetingIdByUserId(userId);
        List<MeetingDto> meetings = meetingIdList.stream()
                .map(i -> meetingRepository.findById(i)
                        .orElseThrow(() -> new EntityNotFoundException("Meeting not found with id: " + i)))
                .map(MeetingDto::toDto)
                .toList();

        return meetings;
    }
}
