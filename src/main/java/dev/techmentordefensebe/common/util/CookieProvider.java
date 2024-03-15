package dev.techmentordefensebe.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieProvider {

    private static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 7;
    private static final String REFRESH_TOKEN_NAME = "refresh_token";

    public ResponseCookie createCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN_NAME, refreshToken)
                .maxAge(COOKIE_MAX_AGE)
                .path("/")
                .secure(true)
                .sameSite(SameSite.NONE.name())
                .httpOnly(false)
                .build();
    }
}
