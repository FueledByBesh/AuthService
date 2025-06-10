package com.lostedin.ecosystem.authservice.dto.session;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.lostedin.ecosystem.authservice.entity.OAuthClientEntity;
import com.lostedin.ecosystem.authservice.enums.OAuthResponseType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionDTO {

    private UUID client_id;
    private UUID user_id;
    private String redirect_uri;
    private String scopes;
    private Instant expires_at;
    private Instant created_at;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    private OAuthClientEntity client;

}
