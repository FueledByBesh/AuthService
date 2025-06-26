package com.lostedin.ecosystem.authservice.entity;

import com.lostedin.ecosystem.authservice.enums.OAuthClientAccessType;
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
     */

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID client_id;
    private String client_secret;
    private OAuthClientAccessType access_type;
    private String app_name;
    private Instant created_at;
//    private String about_app;
//    private String app_icon;

    @PrePersist
    private void setCreated_at() {
        this.created_at = Instant.now();
    }

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.REMOVE)
    private List<OAuthClientURIsEntity> uris;

}
