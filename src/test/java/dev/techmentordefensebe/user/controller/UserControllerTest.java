package dev.techmentordefensebe.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.techmentordefensebe.annotation.WithUserPrincipals;
import dev.techmentordefensebe.oauth.enumtype.OauthProvider;
import dev.techmentordefensebe.user.dto.request.UserAddRequest;
import dev.techmentordefensebe.user.dto.response.UserAddResponse;
import dev.techmentordefensebe.user.service.UserService;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("[컨트롤러]유저")
@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @DisplayName("[POST]유저등록 - 정상호출")
    @WithUserPrincipals
    @Test
    void addUser() throws Exception {
        JSONObject request = new JSONObject();
        String email = "test@google.com";
        String nickname = "tester";
        OauthProvider google = OauthProvider.GOOGLE;
        String profileImgUrl = "https://lh3.googleusercontent.com/a-/ALV-UjXXYOOAgZ1MsaU2w5YOz4J_s0aQfA8JkbCNCWE9eNFrp3Y=s96-c";

        request.put("email", email);
        request.put("nickname", nickname);
        request.put("oauthLoginType", google.name());
        request.put("profileImgUrl", profileImgUrl);

        when(userService.addUser(any(UserAddRequest.class)))
                .thenReturn(createUserAddResponse(email, nickname, google, profileImgUrl));

        mvc.perform(
                        RestDocumentationRequestBuilders.post("/api/v1/users")
                                .with(csrf().asHeader())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request.toString()))
                .andDo(print())
                .andDo(
                        document(
                                "add-user",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("email").type(STRING).description("이메일"),
                                        fieldWithPath("nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("oauthLoginType").type(STRING).description("SNS 로그인 타입"),
                                        fieldWithPath("profileImgUrl").type(STRING).description("프로필 이미지 URL")
                                )
                        )
                )
                .andExpect(status().isCreated());
    }

    private UserAddResponse createUserAddResponse(String email, String nickname, OauthProvider oauthLoginType,
                                                  String profileImgUrl) {
        return new UserAddResponse(1L, email, nickname, oauthLoginType, profileImgUrl);
    }

    @Test
    void addUserTech() {
    }
}