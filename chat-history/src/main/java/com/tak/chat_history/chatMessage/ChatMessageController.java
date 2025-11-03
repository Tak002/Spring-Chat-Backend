package com.tak.chat_history.chatMessage;

import com.tak.common.commonDto.receive.ChatMessageReceiveDto;
import com.tak.common.commonDto.receive.ChatMessagesReceiveDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageRepository chatMessageRepository;

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

    @GetMapping("/{roomId}/messages")
    public ChatMessagesReceiveDto getMessages(@PathVariable String roomId) {
        List<ChatMessageReceiveDto> messages = chatMessageRepository
                .findByRoomIdAndDeletedFalseOrderByCreatedAtAsc(roomId)
                .stream()
                .map(chatMessage -> ChatMessageReceiveDto.of(
                        chatMessage.getId(),
                        chatMessage.getSender(),
                        chatMessage.getContent(),
                        chatMessage.getRoomId(),
                        chatMessage.getCreatedAt(),
                        chatMessage.getEditedAt(),
                        chatMessage.isDeleted()))
                .toList();

        return new ChatMessagesReceiveDto(messages);

    }
}

