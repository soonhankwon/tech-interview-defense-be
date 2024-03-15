package dev.techmentordefensebe.chat.controller;

import dev.techmentordefensebe.chat.dto.request.ChatAddRequest;
import dev.techmentordefensebe.chat.dto.response.ChatAddResponse;
import dev.techmentordefensebe.chat.dto.response.ChatDetailsGetResponse;
import dev.techmentordefensebe.chat.dto.response.ChatGetResponse;
import dev.techmentordefensebe.chat.service.ChatService;
import dev.techmentordefensebe.common.security.impl.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatAddResponse> addChat(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @RequestBody ChatAddRequest request) {
        ChatAddResponse res = chatService.addChat(userDetails, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping
    public ResponseEntity<List<ChatGetResponse>> getChatsByUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ChatGetResponse> res = chatService.findChatsByUser(userDetails);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDetailsGetResponse> getChat(@PathVariable Long chatId) {
        ChatDetailsGetResponse res = chatService.findChatDetails(chatId);
        return ResponseEntity.ok().body(res);
    }
}
