package com.tak.app_service.service;

import com.tak.app_service.dto.forms.*;
import com.tak.app_service.entity.JoinAnswer;
import com.tak.app_service.entity.JoinForm;
import com.tak.app_service.entity.Meeting;
import com.tak.app_service.entity.MeetingMember;
import com.tak.app_service.entity.enums.MeetingMemberRole;
import com.tak.app_service.entity.enums.MeetingMemberState;
import com.tak.app_service.repository.JoinAnswerRepository;
import com.tak.app_service.repository.JoinFormRepository;
import com.tak.app_service.repository.MeetingMemberRepository;
import com.tak.app_service.repository.MeetingRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    private final MeetingMemberRepository meetingMemberRepository;

    public FormResponse createForm(Long userId, FormRequest formRequest) {
        // 폼 생성 로직 구현
        JoinForm save = joinFormRepository.save(JoinForm.builder().userId(userId).questionsJson(formRequest.toJson()).build());
        return formMapper.toFormResponse(save);
    }

    public List<FormResponse> getForms(Long userId) {
        List<JoinForm> joinForms = joinFormRepository.findAllByUserId(userId);
        return joinForms.stream().map(formMapper::toFormResponse).toList();
    }

    @Transactional
    public FormAnswerResponse submitFormAnswer(Long userId, Long meetingId, FormAnswerRequest formAnswerRequest) {
        // 1) 미팅 존재 여부 확인
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new EntityNotFoundException("Meeting not found with id: " + meetingId));

        // 2) 답변 저장
        JoinAnswer joinAnswer;
        try {
            joinAnswer = joinAnswerRepository.save(
                    JoinAnswer.builder()
                            .userId(userId)
                            .meetingId(meetingId)
                            .answersJson(formAnswerRequest.toJson())
                            .build()
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("User has already submitted an answer for this meeting.");
        }

        // 3) 멤버 레코드 생성 (PENDING)
        try {
            meetingMemberRepository.save(
                    MeetingMember.builder()
                            .meetingId(meetingId)
                            .userId(userId)
                            .state(MeetingMemberState.PENDING)
                            .role(MeetingMemberRole.MEMBER)
                            .build()
            );
        } catch (Exception e) {
            // 이미 저장된 멤버인 경우 예외 발생
            throw new IllegalArgumentException("User has already submitted an answer for this meeting.");
        }
        // 4) 응답 DTO 변환
        return formMapper.toAnswerResponse(joinAnswer,meeting.getJoinFormId());
    }

    public List<FormAnswerResponse> getFormAnswersByHostId(Long hostId) {
        List<Long> hostMeetingIds = meetingRepository.findIdByHostId(hostId);
        List<JoinAnswer> joinAnswers = joinAnswerRepository.findAllByMeetingIdIn(hostMeetingIds);
        List<Long> joinFormIds = joinAnswers.stream()
                .map(answer -> meetingRepository.findById(answer.getMeetingId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Meeting not found with id: " + answer.getMeetingId()
                        ))
                        .getJoinFormId()
                )
                .toList();

        return java.util.stream.IntStream.range(0, joinAnswers.size())
                        .mapToObj(i-> formMapper.toAnswerResponse(joinAnswers.get(i),joinFormIds.get(i)))
                        .toList();
    }
}
