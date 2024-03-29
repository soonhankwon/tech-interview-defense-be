package dev.techmentordefensebe.chat.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
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

    @DisplayName("[POST]채팅추가 - 정상호출")
    @WithUserPrincipals
    @Test
    void addChat() throws Exception {
        JSONObject request = new JSONObject();
        String topicTech = "SPRING";
        String mentoringLevel = "BEGINNER";
        String mentorTone = "부드러움";
        boolean isDefenseMode = false;

        request.put("topicTech", topicTech);
        request.put("mentoringLevel", mentoringLevel);
        request.put("mentorTone", mentorTone);
        request.put("isDefenseMode", isDefenseMode);

        when(chatService.addChat(any(UserDetailsImpl.class),
                any(ChatAddRequest.class)))
                .thenReturn(createChatAddResponse(topicTech, mentoringLevel, mentorTone, isDefenseMode));

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
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("topicTech").type(STRING).description("토픽기술"),
                                        fieldWithPath("mentoringLevel").type(STRING).description("멘토링 레벨"),
                                        fieldWithPath("mentorTone").type(STRING).description("멘토대화톤"),
                                        fieldWithPath("isDefenseMode").type(BOOLEAN).description("디펜스모드 여부")
                                )
                        )
                )
                .andExpect(status().isCreated());
    }

    private ChatAddResponse createChatAddResponse(String topicTech, String mentoringLevel, String mentorTone,
                                                  boolean isDefenseMode) {
        return new ChatAddResponse(1L, topicTech, mentoringLevel, mentorTone, isDefenseMode, 1L);
    }

    @DisplayName("[GET]유저채팅 목록조회 - 정상호출")
    @WithUserPrincipals
    @Test
    void getChatsByUser() throws Exception {
        when(chatService.findChatsByUser(any(UserDetailsImpl.class),
                anyInt(), any()))
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
        ChatDTO chat1 = new ChatDTO(1L, "SPRING", false, now);
        ChatDTO chat2 = new ChatDTO(3L, "JAVA", false, now.plusSeconds(1L));
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