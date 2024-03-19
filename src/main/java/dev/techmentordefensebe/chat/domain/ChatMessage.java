package dev.techmentordefensebe.chat.domain;

import dev.techmentordefensebe.common.domain.CreatedTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "`chat_message`")
public class ChatMessage extends CreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @JoinColumn(name = "chat_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Chat chat;

    /* chat user message 가 아닌 경우는 AI 메세지이다.
     * 채팅은 AI 와 채팅방 개설자의 1:1대화로만 이루어진다.
     */
    @Column(name = "is_user_message")
    private Boolean isUserMessage;

    private ChatMessage(String content, Chat chat, Boolean isUserMessage) {
        this.content = content;
        this.chat = chat;
        this.isUserMessage = isUserMessage;
    }

    public static ChatMessage of(String content, Chat chat, Boolean isUserMessage) {
        return new ChatMessage(content, chat, isUserMessage);
    }
}
