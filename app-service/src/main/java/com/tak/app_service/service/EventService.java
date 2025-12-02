package com.tak.app_service.service;

import com.tak.app_service.dto.event.EventCreateRequest;
import com.tak.app_service.entity.Event;
import com.tak.app_service.entity.enums.EventStatus;
import com.tak.app_service.repository.EventRepository;
import com.tak.common.appUser.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public Event getEvent(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        if(event.isEmpty() || event.get().getStatus() == EventStatus.DELETED) {
            return null;
        }
        return event.get();
    }

    public List<Event> listEvents() {
        return eventRepository.findAllByStatus(EventStatus.ACTIVE);
    }

    public List<Event> myEvents(Long userId) {
        return eventRepository.findByOwner(AppUser.builder().id(userId).build());
    }

    public Event createEvent(EventCreateRequest eventCreateRequest, Long userId) {

        Event event = Event.builder()
                .owner(AppUser.builder().id(userId).build())
                .title(eventCreateRequest.title())
                .description(eventCreateRequest.description())
                .place(eventCreateRequest.place())
                .startAt(eventCreateRequest.startAt().atZone(ZoneId.of("Asia/Seoul")).toInstant())
                .endAt(eventCreateRequest.endAt().atZone(ZoneId.of("Asia/Seoul")).toInstant())
                .thumbnailId(eventCreateRequest.thumbnailId())
                .status(EventStatus.ACTIVE)
                .build();
        return eventRepository.save(event);
    }
}
