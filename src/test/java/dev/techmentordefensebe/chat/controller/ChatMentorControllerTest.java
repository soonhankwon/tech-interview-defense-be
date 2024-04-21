package dev.techmentordefensebe.chat.controller;

import static org.mockito.ArgumentMatchers.any;
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
import dev.techmentordefensebe.chat.dto.request.ChatMentorAnswerRequest;
import dev.techmentordefensebe.chat.dto.response.ChatMentorAnswerResponse;
import dev.techmentordefensebe.chat.service.ChatMentorService;
import dev.techmentordefensebe.common.util.JwtProvider;
import java.time.LocalDateTime;
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

@DisplayName("[컨트롤러]멘토링")
@WebMvcTest(ChatMentorController.class)
@AutoConfigureRestDocs
class ChatMentorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ChatMentorService chatMentorService;

    @MockBean
    private JwtProvider jwtProvider;

    @DisplayName("[POST]유저질문추가 + 멘토링응답조회 - 정상호출")
    @WithUserPrincipals
    @Test
    void getMentorAnswer() throws Exception {
        JSONObject request = new JSONObject();
        String content = "DI는 무엇인가요?";

        request.put("content", content);
        request.put("isQuestionGenerated", false);

        when(chatMentorService.findMentorAnswer(anyLong(),
                any(ChatMentorAnswerRequest.class)))
                .thenReturn(createChatMentorAnswerResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.post("/api/v1/mentors/1")
                                .with(csrf().asHeader())
                                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request.toString()))
                .andDo(print())
                .andDo(
                        document(
                                "get-mentor-answer",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .andExpect(status().isOk());
    }

    private static ChatMentorAnswerResponse createChatMentorAnswerResponse() {
        return new ChatMentorAnswerResponse(
                "스프링의 DI(Dependency Injection)는 객체 간의 의존성을 느슨하게 만들어주는 디자인 패턴입니다. 이 패턴은 객체가 직접 자신이 필요로 하는 의존 객체를 생성하거나 관리하는 것이 아니라, 외부에서 의존 객체를 주입받아 사용하는 방식을 말합니다.",
                LocalDateTime.now()
        );
    }
}