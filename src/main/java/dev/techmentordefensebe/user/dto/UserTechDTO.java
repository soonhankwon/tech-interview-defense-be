package dev.techmentordefensebe.user.dto;

import dev.techmentordefensebe.user.domain.UserTech;
import java.util.List;
import java.util.stream.Collectors;

public record UserTechDTO(
        Long techId,
        String techName
) {
    public static List<UserTechDTO> from(List<UserTech> userTechs) {
        return userTechs.stream().map(userTech ->
                new UserTechDTO(
                        userTech.getTech().getId(),
                        userTech.getTech().getName()
                )
        ).collect(Collectors.toList());
    }
}
