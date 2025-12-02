package com.tak.app_service.service;

import com.tak.app_service.entity.Event;
import com.tak.app_service.entity.enums.EventStatus;
import com.tak.app_service.repository.EventRepository;
import com.tak.common.appUser.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
