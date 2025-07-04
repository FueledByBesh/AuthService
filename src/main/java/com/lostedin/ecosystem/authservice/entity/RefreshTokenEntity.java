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

    /*
        Решено использовать opaque токены для refresh token, потому что claims все равно будет сохранен в бд,
        нету смысла рисковать впихая их внутрь токена.
     */

    // TODO: 03.07.2025 не знаю правильно ли я делаю или нет
    @Transient
    private long tokenTTLMillis =24 * 60 * 60 * 1000; // time to live 1 day by default

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "token_id")
    private UUID tokenId;

//    @Lob // будет хранить в бд как large object (в случае postgres как TEXT)
//    @Column(nullable = false, unique = true)
//    private String token;

    // token_hash нужен для быстрого поиска токена, так как text плохо индексируется
    @Column(name = "token_hash", nullable = false,length = 64)
    private String tokenHash;

    @Column(nullable = false)
    private boolean revoked;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;


    @PrePersist
    private void prePersist(){
        this.createdAt = Instant.now();
        this.revoked = false;
        this.expiresAt = this.createdAt.plusMillis(tokenTTLMillis);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", insertable = false, updatable = false)
    private SessionEntity session;

}
