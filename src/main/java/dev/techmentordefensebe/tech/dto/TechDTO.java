package dev.techmentordefensebe.tech.dto;

import dev.techmentordefensebe.tech.domain.Tech;
import dev.techmentordefensebe.user.domain.UserTech;
import java.util.List;
import java.util.stream.Collectors;

public record TechDTO(
        Long id,
        String name
) {
    public static TechDTO from(Tech tech) {
        return new TechDTO(
                tech.getId(),
                tech.getName()
        );
    }

    public static List<TechDTO> from(List<UserTech> userTechs) {
        return userTechs.stream()
                .map(userTech ->
                        new TechDTO(
                                userTech.getId(),
                                userTech.getTech().getName()
                        )
                )
                .collect(Collectors.toList());
    }
}
