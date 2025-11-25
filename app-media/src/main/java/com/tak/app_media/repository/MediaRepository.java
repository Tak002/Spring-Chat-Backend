package com.tak.app_media.repository;

import com.tak.app_media.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Long> {
}
