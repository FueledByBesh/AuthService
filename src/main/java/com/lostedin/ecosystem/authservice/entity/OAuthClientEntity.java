package com.lostedin.ecosystem.authservice.entity;

import com.lostedin.ecosystem.authservice.enums.OAuthClientType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "oauth_clients")
public class OAuthClientEntity {

    /* TODO: (ideas for future)
        1) наверное стоит вывести это на другой микросервис (например DevService)
        2) связать с существующей lostedin.account
        3) профили для приложения
        4) внердрить client trust level для resource owners (в зависимости от безопасного хранения данных)
     */


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "client_id")
    private UUID clientId;
    @Column(name = "client_secret", nullable = false)
    private String clientSecret;
    @Enumerated(EnumType.STRING)
    @Column(name = "client_type",nullable = false)
    private OAuthClientType clientType;
    @Column(name = "app_name")
    private String appName;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
//    private String about_app;
//    private String app_icon;

    @PrePersist
    private void prePersist() {
        this.createdAt = Instant.now();
    }

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.ALL)
    private List<OAuthClientURIsEntity> uris;

}
