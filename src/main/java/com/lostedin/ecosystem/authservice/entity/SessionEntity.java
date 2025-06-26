package com.lostedin.ecosystem.authservice.entity;

import com.lostedin.ecosystem.authservice.enums.OAuthResponseType;
import com.lostedin.ecosystem.authservice.enums.StatusType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Getter
@Setter
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID session_id;
    private UUID client_id;
    private UUID user_id;
    private String redirect_uri;
    private String scopes;
    private StatusType session_status;
    private Instant created_at;

    private int times_token_refreshed;

    @PrePersist
    private void prePersist(){
        this.created_at = Instant.now();
        this.session_status = StatusType.ACTIVE;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    private OAuthClientEntity client;

}
