package com.tak.app_service.repository;

import com.tak.app_service.entity.EventTag;
import com.tak.app_service.entity.EventTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventTagRepository extends JpaRepository<EventTag, EventTagId> {
}
