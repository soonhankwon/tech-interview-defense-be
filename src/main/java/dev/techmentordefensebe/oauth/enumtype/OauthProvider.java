package dev.techmentordefensebe.oauth.enumtype;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OauthProvider {
    KAKAO("kakao"),
    GOOGLE("google"),
    NONE("none");

    private final String value;

    public static void validateProvider(String provider) {
        boolean isValidProvider = Arrays.stream(OauthProvider.values())
                .map(OauthProvider::getValue)
                .anyMatch(i -> i.equals(provider));

        if (!isValidProvider) {
            throw new IllegalArgumentException("invalid provider");
        }
    }
}
