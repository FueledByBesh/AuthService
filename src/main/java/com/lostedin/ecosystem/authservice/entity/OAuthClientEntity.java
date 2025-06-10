package com.lostedin.ecosystem.authservice.entity;

import com.lostedin.ecosystem.authservice.enums.OAuthClientAccessType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "oauth_clients")
public class OAuthClientEntity {

    /* TODO: (idea for future)
        1) наверное стоит вывести это на другой микросервис (например DevService)
        2) связать с существующей lostedin.account
        3) профили для приложения
     */

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID client_id;
    @Column(nullable = false)
    private String client_secret;
    private String redirect_uri;
    private OAuthClientAccessType access_type;
    private String app_name;
//    private String about_app;
//    private String app_icon;

}
