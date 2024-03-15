package dev.techmentordefensebe.user.dto.response;

import dev.techmentordefensebe.user.dto.UserTechDTO;
import java.util.List;

public record UserTechAddResponse(
        List<UserTechDTO> userTechs
) {
    public static UserTechAddResponse from(List<UserTechDTO> userTechDTOS) {
        return new UserTechAddResponse(userTechDTOS);
    }
}
