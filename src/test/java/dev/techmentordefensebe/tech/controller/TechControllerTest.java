package dev.techmentordefensebe.tech.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.techmentordefensebe.annotation.WithUserPrincipals;
import dev.techmentordefensebe.tech.dto.TechDTO;
import dev.techmentordefensebe.tech.dto.response.TechsGetResponse;
import dev.techmentordefensebe.tech.service.TechService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("[컨트롤러]기술")
@WebMvcTest(TechController.class)
@AutoConfigureRestDocs
class TechControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TechService techService;

    @DisplayName("[GET]기술목록조회 - 정상호출")
    @WithUserPrincipals
    @Test
    void getTechs() throws Exception {
        when(techService.findTechs(anyInt()))
                .thenReturn(createTechsGetResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/techs")
                                .param("pageNumber", "0")
                                .with(csrf().asHeader()))
                .andDo(print())
                .andDo(
                        document(
                                "get-techs",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .andExpect(status().isOk());
    }

    private TechsGetResponse createTechsGetResponse() {
        TechDTO dto1 = new TechDTO(1L, "SPRING");
        TechDTO dto2 = new TechDTO(2L, "JAVA");

        return new TechsGetResponse(1, List.of(dto1, dto2));
    }
}