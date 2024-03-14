package dev.techmentordefensebe.oauth.enumtype;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OauthProviderTest {

    @Test
    void validateProvider() {
    }

    @Test
    void getName() {
        assertThat(OauthProvider.KAKAO.getName()).isEqualTo("kakao");
    }

    @Test
    void values() {
        OauthProvider[] values = OauthProvider.values();
        assertThat(values.length).isEqualTo(2);
    }

    @Test
    void valueOf() {
        assertThat(OauthProvider.valueOf("KAKAO")).isEqualTo(OauthProvider.KAKAO);
    }
}