package dev.techmentordefensebe.oauth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.techmentordefensebe.annotation.WithUserPrincipals;
import dev.techmentordefensebe.oauth.dto.OauthLoginResponse;
import dev.techmentordefensebe.oauth.service.OauthService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("[컨트롤러]Oauth")
@WebMvcTest(OauthController.class)
@AutoConfigureRestDocs
class OauthControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OauthService oauthService;

    @DisplayName("[GET]Oauth 로그인 - 정상호출")
    @WithUserPrincipals
    @Test
    void oauthLogin() throws Exception {
        String email = "test@gmail.com";
        String nickname = "tester";
        String profileImgUrl = "https://blahblah.com/1234456";
        when(oauthService.login(anyString(), anyString(), any(HttpServletResponse.class)))
                .thenReturn(createOauthLoginResponse(email, nickname, profileImgUrl));

        mvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/oauth/kakao")
                                .with(csrf().asHeader())
                                .param("code", UUID.randomUUID().toString()))
                .andDo(print())
                .andDo(
                        document(
                                "get-oauth-login",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .andExpect(status().isOk());
    }

    private static OauthLoginResponse createOauthLoginResponse(String email, String nickname, String profileImgUrl) {
        UUID uuid = UUID.randomUUID();
        return new OauthLoginResponse(uuid.toString(), email, nickname, profileImgUrl, false);
    }
}