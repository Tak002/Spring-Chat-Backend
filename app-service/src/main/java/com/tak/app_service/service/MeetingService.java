package com.tak.app_service.service;

import com.tak.app_service.dto.meeting.MeetingCreateRequest;
import com.tak.app_service.dto.meeting.MeetingDto;
import com.tak.app_service.entity.Event;
import com.tak.app_service.entity.Media;
import com.tak.app_service.entity.Meeting;
import com.tak.app_service.repository.MeetingRepository;
import com.tak.common.appUser.AppUser;
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
                .thumbnail(Media.builder().id(meetingCreateRequest.thumbnailId()).build())
                .linkedEvent(Event.builder().id(meetingCreateRequest.linkedEventId()).build())
                .gender(meetingCreateRequest.rules().gender())
                .minAge(meetingCreateRequest.rules().ageRange().min())
                .maxAge(meetingCreateRequest.rules().ageRange().max())
                .build();

        return MeetingDto.toDto(meetingRepository.save(meeting));
    }
    public List<MeetingDto> getMeetings() {
        List<Meeting> all = meetingRepository.findAll();
        return all.stream().map(MeetingDto::toDto).collect(Collectors.toList());
    }

    public MeetingDto getMeeting(Long id) {
        Meeting meeting = meetingRepository.findById(id).orElse(null);
        if (meeting == null) {
            return null;
        }
        return MeetingDto.toDto(meeting);
    }
}
