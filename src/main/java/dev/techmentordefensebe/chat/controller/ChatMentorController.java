package dev.techmentordefensebe.chat.controller;

import dev.techmentordefensebe.chat.dto.request.ChatMentorAnswerRequest;
import dev.techmentordefensebe.chat.dto.response.ChatMentorAnswerResponse;
import dev.techmentordefensebe.chat.service.ChatMentorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/mentors")
public class ChatMentorController {

    private final ChatMentorService chatMentorService;

    @PostMapping("/{chatId}")
    public ChatMentorAnswerResponse getMentorAnswer(@PathVariable Long chatId,
                                                    @RequestBody ChatMentorAnswerRequest request) {
        return chatMentorService.findMentorAnswer(chatId, request);
    }
}
