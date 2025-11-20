package com.tak.app_service.repository;

import com.tak.app_service.entity.MeetingTag;
import com.tak.app_service.entity.MeetingTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingTagRepository extends JpaRepository<MeetingTag, MeetingTagId> {
}
