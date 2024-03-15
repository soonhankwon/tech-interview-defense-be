package dev.techmentordefensebe.user.dto.request;

import dev.techmentordefensebe.tech.domain.Tech;
import dev.techmentordefensebe.user.domain.User;
import dev.techmentordefensebe.user.domain.UserTech;

public record UserTechAddRequest(
        String techName
) {
    public UserTech toEntity(User user, Tech tech) {
        return UserTech.of(user, tech);
    }
}
