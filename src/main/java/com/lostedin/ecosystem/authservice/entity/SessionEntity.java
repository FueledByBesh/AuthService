package com.lostedin.ecosystem.authservice.entity;

import com.lostedin.ecosystem.authservice.enums.OAuthResponseType;
import com.lostedin.ecosystem.authservice.enums.StatusType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Getter
@Setter
public class SessionEntity {

    // TODO: 03.07.2025
    //  Надо обдумать хранить ли данные о сессии вечно или удалить после окончания
    //  Первая мысль: Думаю лучше хранить о них коротко в логах или вообще не удалять


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "session_id")
    private UUID sessionId;
    @Column(name = "client_id")
    private UUID clientId;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "redirect_uri")
    private String redirectURI;
    private String scopes;
    @Column(name = "session_status")
    private StatusType sessionStatus;
    @Column(name = "created_at")
    private Instant createdAt;


    @PrePersist
    private void prePersist(){
        this.createdAt = Instant.now();
        this.sessionStatus = StatusType.ACTIVE;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    private OAuthClientEntity client;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<RefreshTokenEntity> refreshTokens;


}
