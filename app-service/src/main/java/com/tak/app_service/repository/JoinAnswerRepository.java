package com.tak.app_service.repository;

import com.tak.app_service.entity.JoinAnswer;
import com.tak.app_service.entity.Meeting;
import com.tak.app_service.entity.JoinFormQuestion;
import com.tak.common.appUser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JoinAnswerRepository extends JpaRepository<JoinAnswer, Long> {

    List<JoinAnswer> findByMeetingAndUser(Meeting meeting, AppUser user);

    Optional<JoinAnswer> findByMeetingAndUserAndQuestion(
            Meeting meeting,
            AppUser user,
            JoinFormQuestion question
    );
}
