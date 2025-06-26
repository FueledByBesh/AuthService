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
    private UUID pre_session_id;
    private UUID client_id;
    private String state;
    private String redirect_uri;
    private String scopes;
    private OAuthResponseType response_type;
    private Instant created_at;

    private UUID user_id;
    private String auth_code;
    private Instant code_expires_at;

    @PrePersist
    private void setCreatedAtDate(){
        this.created_at = Instant.now();
    }


}
