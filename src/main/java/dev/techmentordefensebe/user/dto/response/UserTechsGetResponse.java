package dev.techmentordefensebe.user.dto.response;

import dev.techmentordefensebe.user.dto.UserTechDTO;
import java.util.List;

public record UserTechsGetResponse(
        List<UserTechDTO> userTechs
) {
    public static UserTechsGetResponse from(List<UserTechDTO> userTechDTOS) {
        return new UserTechsGetResponse(userTechDTOS);
    }
}
