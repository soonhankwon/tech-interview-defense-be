package dev.techmentordefensebe.chat.controller;

import dev.techmentordefensebe.chat.dto.request.ChatAddRequest;
import dev.techmentordefensebe.chat.dto.response.ChatAddResponse;
import dev.techmentordefensebe.chat.dto.response.ChatDeleteResponse;
import dev.techmentordefensebe.chat.dto.response.ChatDetailsGetResponse;
import dev.techmentordefensebe.chat.dto.response.ChatsGetResponse;
import dev.techmentordefensebe.chat.service.ChatService;
import dev.techmentordefensebe.common.security.impl.UserDetailsImpl;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<ChatsGetResponse> getChatsByUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @RequestParam int pageNumber,
                                                           @RequestParam(required = false)
                                                           @Pattern(regexp = "^defense", message = "mode는 defense만 가능합니다.") String mode) {
        ChatsGetResponse res = chatService.findChatsByUser(userDetails, pageNumber, mode);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDetailsGetResponse> getChat(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @PathVariable Long chatId) {
        ChatDetailsGetResponse res = chatService.findChatDetails(userDetails, chatId);
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<ChatDeleteResponse> deleteChat(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @PathVariable Long chatId) {
        ChatDeleteResponse res = chatService.deleteChat(userDetails, chatId);
        return ResponseEntity.ok().body(res);
    }
}
