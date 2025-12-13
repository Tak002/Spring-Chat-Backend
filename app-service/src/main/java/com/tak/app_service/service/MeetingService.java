package com.tak.app_service.service;

import com.tak.app_service.dto.meeting.MeetingCreateRequest;
import com.tak.app_service.dto.meeting.MeetingDetailDto;
import com.tak.app_service.dto.meeting.MeetingDto;
import com.tak.app_service.entity.JoinAnswer;
import com.tak.app_service.entity.Meeting;
import com.tak.app_service.entity.MeetingMember;
import com.tak.app_service.entity.enums.MeetingMemberState;
import com.tak.app_service.repository.*;
import com.tak.common.appUser.AppUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    private final AppUserRepository appUserRepository;
    private final JoinAnswerRepository joinAnswerRepository;

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
                .joinFormId(meetingCreateRequest.joinFormId())
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
        return all.stream().map(MeetingDto::toDto).collect(toList());
    }

    public MeetingDetailDto getMeetingDetails(Long id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meeting not found with id: " + id));
        List<Long> memberIdList = meetingMemberRepository.findUserIdByMeetingId(id);
        List<AppUser> members = appUserRepository.findAllById(memberIdList);
        return MeetingDetailDto.from(meeting, members);
    }

    public List<MeetingDto> getMyMeetings(Long userId) {
        List<Long> meetingIdList = meetingMemberRepository.findMeetingIdByUserId(userId);
        List<Meeting> meetings = meetingRepository.findAllById(meetingIdList);
        return meetings.stream()
                .map(MeetingDto::toDto)
                .toList();
    }

    @Transactional
    public void deleteMeeting(Long id, Long userId) {
        Meeting meeting = meetingRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Meeting not found with id: " + id));
        Long hostId = meeting.getHostId();
        if(!hostId.equals(userId)) {
            throw new IllegalArgumentException("Only the host can delete the meeting.");
        }
        meetingMemberRepository.deleteAllByMeetingId(id);
        meetingRepository.deleteById(id);
    }

    public List<MeetingDto> getMeetingsByEventId(Long eventId) {
        List<Meeting> meetings = meetingRepository.findByLinkedEventId(eventId);
        return meetings.stream().map(MeetingDto::toDto).collect(toList());
    }

    public List<MeetingDto> getMeetingsByTitleKeyword(String keyword) {
        List<Meeting> meetings = meetingRepository.findByTitleContains(keyword);
        return meetings.stream().map(MeetingDto::toDto).toList();
    }

    public boolean isNeedFormAnswer(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new EntityNotFoundException("Meeting not found with id: " + meetingId));
        return meeting.getJoinFormId() != null;
    }
    // 유저 승인하기


    // 유저를 미팅에 넣기
    public MeetingMember addUserToMeeting(Long meetingId, Long userId) {
        MeetingMember meetingMember = MeetingMember.builder()
                .meetingId(meetingId)
                .userId(userId)
                .role(com.tak.app_service.entity.enums.MeetingMemberRole.MEMBER)
                .state(MeetingMemberState.APPROVED)
                .build();
        return meetingMemberRepository.save(meetingMember);
    }

    @Transactional
    public void approveAnswer(Long hostId, Long answerId) {
        // 1) 폼 응답 조회
        JoinAnswer joinAnswer = joinAnswerRepository.findById(answerId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Form answer not found with id: " + answerId));

        Long meetingId = joinAnswer.getMeetingId();
        Long userId = joinAnswer.getUserId();

        // 2) 미팅 조회 + host 권한 체크
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Meeting not found with id: " + meetingId));

        if (!meeting.getHostId().equals(hostId)) {
            throw new IllegalArgumentException("Only the host can approve form answers.");
        }

        // 3) 신청한 유저의 MeetingMember 찾기 (보통 PENDING 상태여야 함)
        MeetingMember meetingMember = meetingMemberRepository.findByUserIdAndMeetingId(userId, meetingId).orElseThrow();

        // 이미 승인된 경우 방어
        if (meetingMember.getState() == MeetingMemberState.APPROVED) {
            throw new IllegalArgumentException("User is already approved for this meeting.");
        }

        // 4) 상태를 APPROVED 로 변경
        meetingMember.setState(MeetingMemberState.APPROVED);
        joinAnswerRepository.deleteById(joinAnswer.getId());
    }

}
