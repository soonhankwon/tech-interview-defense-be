package dev.techmentordefensebe.tech.dto;

import dev.techmentordefensebe.tech.domain.Tech;

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
}
