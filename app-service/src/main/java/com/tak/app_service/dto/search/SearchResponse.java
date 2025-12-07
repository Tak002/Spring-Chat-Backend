package com.tak.app_service.dto.search;

import com.tak.app_service.dto.event.EventDto;
import com.tak.app_service.dto.meeting.MeetingDto;

import java.util.List;

public record SearchResponse(
        List<EventDto> events,
        List<MeetingDto> meetings
) {
}
