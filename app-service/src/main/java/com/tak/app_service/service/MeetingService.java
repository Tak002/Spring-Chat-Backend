package com.tak.app_service.service;

import com.tak.app_service.dto.meeting.MeetingCreateRequest;
import com.tak.app_service.dto.meeting.MeetingDto;
import com.tak.app_service.entity.Event;
import com.tak.app_service.entity.Meeting;
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

    public MeetingDto createMeeting(MeetingCreateRequest meetingCreateRequest, Long hostId) {
        Meeting meeting = Meeting.builder()
                .host(AppUser.builder().id(hostId).build())
                .title(meetingCreateRequest.title())
                .description(meetingCreateRequest.description())
                .startAt(meetingCreateRequest.startAt())
                .endAt(meetingCreateRequest.endAt())
                .place(meetingCreateRequest.place())
                .maxMembers(meetingCreateRequest.participantLimit())
                .thumbnailId(meetingCreateRequest.thumbnailId())
                .linkedEvent(meetingCreateRequest.linkedEventId() != null
                        ? Event.builder().id(meetingCreateRequest.linkedEventId()).build() : null)
                .gender(meetingCreateRequest.rules() != null ? meetingCreateRequest.rules().gender() : null)
                .minAge(meetingCreateRequest.rules() != null && meetingCreateRequest.rules().ageRange() != null
                        ? meetingCreateRequest.rules().ageRange().min() : null)
                .maxAge(meetingCreateRequest.rules() != null && meetingCreateRequest.rules().ageRange() != null
                        ? meetingCreateRequest.rules().ageRange().max() : null)
                .build();

        return MeetingDto.toDto(meetingRepository.save(meeting));
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
