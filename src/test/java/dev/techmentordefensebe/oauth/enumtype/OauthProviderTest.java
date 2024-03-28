package dev.techmentordefensebe.oauth.enumtype;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class OauthProviderTest {

    @Test
    void validateProvider_wrong_provider_exception() {
        assertThatThrownBy(() -> OauthProvider.validateProvider("wrong"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getValue() {
        assertThat(OauthProvider.KAKAO.getValue()).isEqualTo("kakao");
    }

    @Test
    void values() {
        OauthProvider[] values = OauthProvider.values();
        assertThat(values.length).isEqualTo(3);
    }

    @Test
    void valueOf() {
        assertThat(OauthProvider.valueOf("KAKAO")).isEqualTo(OauthProvider.KAKAO);
    }
}