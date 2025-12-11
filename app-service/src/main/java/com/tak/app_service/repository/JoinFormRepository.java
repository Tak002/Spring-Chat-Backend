package com.tak.app_service.repository;

import com.tak.app_service.entity.JoinForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JoinFormRepository extends JpaRepository<JoinForm, Long> {
    List<JoinForm> findAllByUserId(Long userId);
}
