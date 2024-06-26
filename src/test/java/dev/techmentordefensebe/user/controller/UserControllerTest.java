package dev.techmentordefensebe.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import dev.techmentordefensebe.common.security.impl.UserDetailsImpl;
import dev.techmentordefensebe.common.util.JwtProvider;
import dev.techmentordefensebe.oauth.enumtype.OauthProvider;
import dev.techmentordefensebe.tech.dto.TechDTO;
import dev.techmentordefensebe.user.dto.UserTechDTO;
import dev.techmentordefensebe.user.dto.request.UserAddRequest;
import dev.techmentordefensebe.user.dto.request.UserTechAddRequest;
import dev.techmentordefensebe.user.dto.response.UserAddResponse;
import dev.techmentordefensebe.user.dto.response.UserTechAddResponse;
import dev.techmentordefensebe.user.dto.response.UserTechDeleteResponse;
import dev.techmentordefensebe.user.dto.response.UserTechsGetResponse;
import dev.techmentordefensebe.user.service.UserService;
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

@DisplayName("[컨트롤러]유저")
@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtProvider jwtProvider;

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

    @DisplayName("[POST]유저기술등록 - 정상호출")
    @WithUserPrincipals
    @Test
    void addUserTech() throws Exception {
        JSONObject request = new JSONObject();
        String techName = "SPRING";
        request.put("techName", techName);

        when(userService.addUserTech(any(UserDetailsImpl.class), any(UserTechAddRequest.class)))
                .thenReturn(createUserTechAddResponse(techName));

        mvc.perform(
                        RestDocumentationRequestBuilders.post("/api/v1/users/techs")
                                .with(csrf().asHeader())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request.toString()))
                .andDo(print())
                .andDo(
                        document(
                                "add-user-tech",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("techName").type(STRING).description("유저기술이름")
                                )
                        )
                )
                .andExpect(status().isCreated());
    }

    private UserTechAddResponse createUserTechAddResponse(String techName) {
        TechDTO existsTechDTO = new TechDTO(1L, "JAVA");
        TechDTO techDTO = new TechDTO(2L, techName);
        return UserTechAddResponse.from(List.of(existsTechDTO, techDTO));
    }

    @DisplayName("[POST]유저기술 목록조회 - 정상호출")
    @WithUserPrincipals
    @Test
    void getUserTechs() throws Exception {
        when(userService.getUserTechs(any(UserDetailsImpl.class)))
                .thenReturn(createUserTechsGetResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/users/techs")
                                .with(csrf().asHeader())
                                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID()))
                .andDo(print())
                .andDo(
                        document(
                                "get-user-techs",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .andExpect(status().isOk());
    }

    private UserTechsGetResponse createUserTechsGetResponse() {
        UserTechDTO userTechDTO1 = new UserTechDTO(1L, 1L, "JAVA");
        UserTechDTO userTechDTO2 = new UserTechDTO(2L, 2L, "SPRING");
        return UserTechsGetResponse.from(List.of(userTechDTO1, userTechDTO2));
    }

    @DisplayName("[POST]유저기술 삭제 - 정상호출")
    @WithUserPrincipals
    @Test
    void deleteUserTech() throws Exception {
        when(userService.deleteUserTech(any(UserDetailsImpl.class), anyLong()))
                .thenReturn(createUserTechsDeleteResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/v1/users/techs/13")
                                .with(csrf().asHeader())
                                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID()))
                .andDo(print())
                .andDo(
                        document(
                                "delete-user-tech",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .andExpect(status().isOk());
    }

    private UserTechDeleteResponse createUserTechsDeleteResponse() {
        return UserTechDeleteResponse.from(13L);
    }
}