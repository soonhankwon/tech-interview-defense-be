package dev.techmentordefensebe.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OauthTokenDTO(
        @JsonProperty(value = "token_type")
        String tokeType,

        @JsonProperty(value = "access_token")
        String accessToken,

        @JsonProperty(value = "expires_in")
        Integer expiresIn,

        @JsonProperty(value = "refresh_token")
        String refreshToken,

        @JsonProperty(value = "refresh_token_expires_in")
        Integer refreshTokenExpiresIn
) {
}
