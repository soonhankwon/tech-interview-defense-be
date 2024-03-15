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

    public User(UserAddRequest request) {
        this.email = request.email();
        String requestNickname = request.nickname();
        // external oauth API 특성상 닉네임이 null or empty 인 경우 default 값 세팅
        if (requestNickname == null || requestNickname.isEmpty()) {
            this.nickname = DEFAULT_NICKNAME;
        } else {
            this.nickname = request.nickname();
        }
        this.oauthLoginType = request.oauthLoginType();
        this.profileImgUrl = request.profileImgUrl();
    }
}
