package com.lostedin.ecosystem.authservice.entity;

//TODO: Реализовать в Redis
// (Кратковременный память до создания полноценной сессий)
// PS: нужен что бы каждый раз не передавать данные в параметрах url

import com.lostedin.ecosystem.authservice.enums.OAuthResponseType;
import jakarta.persistence.*;
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
    @Column(name = "pre_session_id")
    private UUID preSessionId;
    @Column(name = "client_id", nullable = false, updatable = false)
    private UUID clientId;
    private String state;
    @Column(name = "redirect_uri")
    private String redirectUri;
    private String scopes;
    @Column(name = "response_type")
    private OAuthResponseType responseType;
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "auth_code")
    private String authCode;
    @Column(name = "code_expires_at")
    private Instant codeExpiresAt;

    @PrePersist
    private void setCreatedAtDate(){
        this.createdAt = Instant.now();
    }


}
