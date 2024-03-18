package dev.techmentordefensebe.tech.dto.response;

import dev.techmentordefensebe.tech.dto.TechDTO;
import java.util.List;

public record TechsGetResponse(
        int totalPages,
        List<TechDTO> techs
) {
    public static TechsGetResponse of(int totalPages, List<TechDTO> techDTOS) {
        return new TechsGetResponse(totalPages, techDTOS);
    }
}
