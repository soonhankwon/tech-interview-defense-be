package dev.techmentordefensebe.user.domain;

import dev.techmentordefensebe.common.domain.BaseTimeEntity;
import dev.techmentordefensebe.oauth.enumtype.OauthProvider;
import dev.techmentordefensebe.user.dto.request.UserAddRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "`user`")
public class User extends BaseTimeEntity {

    private static final String DEFAULT_NICKNAME = "Anonymous";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "oauth_login_type", nullable = false)
    private OauthProvider oauthLoginType;

    @Column(name = "profile_img_url")
    private String profileImgUrl;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<UserTech> userTechs = new ArrayList<>();

    private User(String email, String nickname, OauthProvider oauthProvider, String profileImgUrl) {
        this.email = email;
        // external oauth API 특성상 닉네임이 null or empty 인 경우 default 값 세팅
        if (nickname == null || nickname.isEmpty()) {
            this.nickname = DEFAULT_NICKNAME;
        } else {
            this.nickname = nickname;
        }
        this.oauthLoginType = oauthProvider;
        this.profileImgUrl = profileImgUrl;
    }

    public static User toEntity(UserAddRequest request) {
        return new User(
                request.email(),
                request.nickname(),
                request.oauthLoginType(),
                request.profileImgUrl()
        );
    }
}
