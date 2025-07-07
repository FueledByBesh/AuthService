package com.lostedin.ecosystem.authservice.entity;

//TODO: Реализовать в Redis
// (Кратковременный память до создания полноценной сессий)
// PS: нужен что бы каждый раз не передавать данные в параметрах url

import com.lostedin.ecosystem.authservice.enums.OAuthFlowParameterTypes.CodeChallengeMethodType;
import com.lostedin.ecosystem.authservice.enums.OAuthFlowParameterTypes.OAuthResponseType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pre_sessions")
@Getter
@Setter
public class PreSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pre_session_id",unique = true)
    @Setter(AccessLevel.NONE)
    private UUID preSessionId;
    @Column(name = "client_id", nullable = false, updatable = false)
    private UUID clientId;
    private String state;
    @Column(name = "redirect_uri",nullable = false)
    private String redirectURI;
    @Column(name = "scopes",nullable = false)
    private String scopes;
    @Column(name = "response_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private OAuthResponseType responseType;
    @Column(name = "created_at",nullable = false)
    @Setter(AccessLevel.NONE)
    private Instant createdAt;

    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "auth_code")
    private String authCode;
    @Column(name = "code_expires_at")
    private Instant codeExpiresAt;

    @Column(name = "code_challenge",updatable = false)
    private String codeChallenge;
    @Column(name = "code_challenge_method",updatable = false)
    @Enumerated(EnumType.STRING)
    private CodeChallengeMethodType codeChallengeMethod;

    @PrePersist
    private void setCreatedAtDate(){
        this.createdAt = Instant.now();
    }


}
