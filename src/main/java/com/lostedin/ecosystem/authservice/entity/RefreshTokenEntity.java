package com.lostedin.ecosystem.authservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
public class RefreshTokenEntity {

    // TODO: 03.07.2025 не знаю правильно ли я делаю или нет 
    @Transient
    private long token_expire_time_millis;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String token;

    private boolean revoked;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant expiresAt;


    @PrePersist
    private void prePersist(){
        this.createdAt = Instant.now();
        this.revoked = false;
        this.expiresAt = this.createdAt.plusMillis(token_expire_time_millis);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", insertable = false, updatable = false)
    private SessionEntity session;

}
