package dev.techmentordefensebe.user.dto.response;

import dev.techmentordefensebe.tech.dto.TechDTO;
import java.util.List;

public record UserTechAddResponse(
        List<TechDTO> userTechs
) {
    public static UserTechAddResponse from(List<TechDTO> techDTO) {
        return new UserTechAddResponse(techDTO);
    }
}
