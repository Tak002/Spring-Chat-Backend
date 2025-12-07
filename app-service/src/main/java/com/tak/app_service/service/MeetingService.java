package com.tak.app_service.service;

import com.tak.app_service.dto.meeting.MeetingCreateRequest;
import com.tak.app_service.dto.meeting.MeetingDto;
import com.tak.app_service.entity.Meeting;
import com.tak.app_service.entity.MeetingMember;
import com.tak.app_service.entity.enums.MeetingMemberState;
import com.tak.app_service.repository.MeetingMemberRepository;
import com.tak.app_service.repository.MeetingRepository;
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
        MeetingMember save = meetingMemberRepository.save(host);
        return MeetingDto.toDto(newMeeting);
    }
    public List<MeetingDto> getMeetings() {
        List<Meeting> all = meetingRepository.findAll();
        return all.stream().map(MeetingDto::toDto).collect(Collectors.toList());
    }

    public MeetingDto getMeeting(Long id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meeting not found with id: " + id));
        return MeetingDto.toDto(meeting);
    }
}
