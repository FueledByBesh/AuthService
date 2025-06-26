package com.lostedin.ecosystem.authservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "oauth_client_uris")
@Getter
@Setter
public class OAuthClientURIsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID uri_id;
    public String uri;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", updatable = false)
    public OAuthClientEntity client;

}
