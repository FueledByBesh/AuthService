package com.lostedin.ecosystem.authservice.dto.session;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lostedin.ecosystem.authservice.enums.OAuthResponseType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreSessionDTO {

    private UUID pre_session_id;
    private UUID client_id;
    private String state;
    private String redirect_uri;
    private OAuthResponseType responseType;
    private String scopes;
    private Instant created_at;

}
