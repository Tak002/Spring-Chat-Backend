package com.tak.app_service.repository;

import com.tak.app_service.entity.Event;
import com.tak.app_service.entity.enums.EventStatus;
import com.tak.common.appUser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByOwner(AppUser owner);

    List<Event> findByStatusAndStartAtBetween(
            EventStatus status,
            Instant from,
            Instant to
    );
}
