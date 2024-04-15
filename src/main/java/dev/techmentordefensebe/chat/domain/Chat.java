package dev.techmentordefensebe.chat.domain;

import dev.techmentordefensebe.common.domain.BaseTimeEntity;
import dev.techmentordefensebe.openai.util.PromptGenerator;
import dev.techmentordefensebe.tech.domain.Tech;
import dev.techmentordefensebe.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "`chat`")
public class Chat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "tech_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Tech tech;

    @Embedded
    private ChatMentor chatMentor;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ChatMessage> chatMessages = new ArrayList<>();

    private Chat(User user, Tech tech, ChatMentor chatMentor) {
        this.user = user;
        this.tech = tech;
        this.chatMentor = chatMentor;
        this.chatMessages.add(
                ChatMessage.of(
                        PromptGenerator.welcomePrompt(this.tech),
                        this,
                        false)
        );
    }

    public static Chat of(User user, Tech tech, ChatMentor chatMentor) {
        return new Chat(user, tech, chatMentor);
    }
}
