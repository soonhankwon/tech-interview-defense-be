package dev.techmentordefensebe.chat.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.techmentordefensebe.annotation.WithUserPrincipals;
import dev.techmentordefensebe.chat.dto.ChatDTO;
import dev.techmentordefensebe.chat.dto.ChatMessageDTO;
import dev.techmentordefensebe.chat.dto.request.ChatAddRequest;
import dev.techmentordefensebe.chat.dto.response.ChatAddResponse;
import dev.techmentordefensebe.chat.dto.response.ChatDeleteResponse;
import dev.techmentordefensebe.chat.dto.response.ChatDetailsGetResponse;
import dev.techmentordefensebe.chat.dto.response.ChatsGetResponse;
import dev.techmentordefensebe.chat.service.ChatService;
import dev.techmentordefensebe.common.security.impl.UserDetailsImpl;
import dev.techmentordefensebe.common.util.JwtProvider;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

/*
 * Response 레코드 생성시 eg)Chat or Tech 같은 객체를 매개변수로 받아 생성하는 경우 static factory method 사용이 어렵고 코드가 길어짐
 * 따라서, 해당 경우에는 생성자를 사용하여 Response 생성
 */
@DisplayName("[컨트롤러]채팅")
@WebMvcTest(ChatController.class)
@AutoConfigureRestDocs
class ChatControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ChatService chatService;

    @MockBean
    private JwtProvider jwtProvider;

    @DisplayName("[POST]채팅추가 - 정상호출")
    @WithUserPrincipals
    @Test
    void addChat() throws Exception {
        JSONObject request = new JSONObject();
        String topicTech = "SPRING";
        String mentoringLevel = "BEGINNER";
        String mentorTone = "부드러움";

        request.put("topicTech", topicTech);
        request.put("mentoringLevel", mentoringLevel);
        request.put("mentorTone", mentorTone);

        when(chatService.addChat(any(UserDetailsImpl.class),
                any(ChatAddRequest.class)))
                .thenReturn(createChatAddResponse(topicTech, mentoringLevel, mentorTone));

        mvc.perform(
                        RestDocumentationRequestBuilders.post("/api/v1/chats")
                                .with(csrf().asHeader())
                                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request.toString()))
                .andDo(print())
                .andDo(
                        document(
                                "add-chat",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .andExpect(status().isCreated());
    }

    private ChatAddResponse createChatAddResponse(String topicTech, String mentoringLevel, String mentorTone) {
        return new ChatAddResponse(1L, topicTech, mentoringLevel, mentorTone, 1L);
    }

    @DisplayName("[GET]유저채팅 목록조회 - 정상호출")
    @WithUserPrincipals
    @Test
    void getChatsByUser() throws Exception {
        when(chatService.findChatsByUser(any(UserDetailsImpl.class), anyInt()))
                .thenReturn(createChatsGetResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/chats")
                                .param("pageNumber", "0")
                                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID())
                                .with(csrf().asHeader()))
                .andDo(print())
                .andDo(
                        document(
                                "get-chats-by-user",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .andExpect(status().isOk());
    }

    private ChatsGetResponse createChatsGetResponse() {
        LocalDateTime now = LocalDateTime.now();
        ChatDTO chat1 = new ChatDTO(1L, "SPRING", now);
        ChatDTO chat2 = new ChatDTO(3L, "JAVA", now.plusSeconds(1L));
        return ChatsGetResponse.of(1, List.of(chat1, chat2));
    }

    @DisplayName("[GET]유저채팅 상세조회 - 정상호출")
    @WithUserPrincipals
    @Test
    void getChat() throws Exception {
        when(chatService.findChatDetails(any(), anyLong()))
                .thenReturn(createChatDetailsGetResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/chats/1")
                                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID())
                                .with(csrf().asHeader()))
                .andDo(print())
                .andDo(
                        document(
                                "get-chat-detail",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .andExpect(status().isOk());
    }

    private ChatDetailsGetResponse createChatDetailsGetResponse() {
        LocalDateTime now = LocalDateTime.now();
        ChatMessageDTO dto1 = new ChatMessageDTO("안녕하세요!", false, now);
        ChatMessageDTO dto2 = new ChatMessageDTO("DI란 무엇인가요?", true, now.plusSeconds(1L));
        return new ChatDetailsGetResponse(1L, 1L, List.of(dto1, dto2));
    }

    @DisplayName("[DELETE]채팅삭제 - 정상호출")
    @WithUserPrincipals
    @Test
    void deleteChat() throws Exception {
        when(chatService.deleteChat(any(), anyLong()))
                .thenReturn(createChatDeleteResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/v1/chats/1")
                                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID())
                                .with(csrf().asHeader()))
                .andDo(print())
                .andDo(
                        document(
                                "delete-chat",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .andExpect(status().isOk());
    }

    private ChatDeleteResponse createChatDeleteResponse() {
        return ChatDeleteResponse.from(true);
    }
}