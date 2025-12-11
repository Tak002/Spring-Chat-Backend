package com.tak.app_service.service;

import com.tak.app_service.dto.forms.*;
import com.tak.app_service.entity.JoinAnswer;
import com.tak.app_service.entity.JoinForm;
import com.tak.app_service.repository.JoinAnswerRepository;
import com.tak.app_service.repository.JoinFormRepository;
import com.tak.app_service.repository.MeetingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormService {
    private final JoinFormRepository joinFormRepository;
    private final JoinAnswerRepository joinAnswerRepository;
    private final MeetingRepository meetingRepository;

    private final FormMapper formMapper;
    public FormResponse createForm(Long userId, FormRequest formRequest) {
        // 폼 생성 로직 구현
        JoinForm save = joinFormRepository.save(JoinForm.builder().userId(userId).questionsJson(formRequest.toJson()).build());
        return formMapper.toFormResponse(save);
    }

    public List<FormResponse> getForms(Long userId) {
        List<JoinForm> joinForms = joinFormRepository.findAllByUserId(userId);
        return joinForms.stream().map(formMapper::toFormResponse).toList();
    }

    public FormAnswerResponse submitFormAnswer(Long userId, Long meetingId, FormAnswerRequest formAnswerRequest) {
        meetingRepository.findById(meetingId).orElseThrow(() -> new EntityNotFoundException("Meeting not found with id: " + meetingId));
        JoinAnswer joinAnswer = joinAnswerRepository.save(JoinAnswer.builder().userId(userId).meetingId(meetingId).answersJson(formAnswerRequest.toJson()).build());
        return formMapper.toAnswerResponse(joinAnswer);
    }


    public List<FormAnswerResponse> getFormAnswersByHostId(Long hostId) {
        List<Long> hostMeetingIds = meetingRepository.findIdByHostId(hostId);
        List<JoinAnswer> joinAnswers = joinAnswerRepository.findAllByMeetingIdIn(hostMeetingIds);
        return joinAnswers.stream().map(formMapper::toAnswerResponse).toList();
    }
}
