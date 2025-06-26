package com.lostedin.ecosystem.authservice.dto.session;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.lostedin.ecosystem.authservice.entity.OAuthClientEntity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionCreateDTO {

    @NotNull(message = "session_id cannot be null")
    private UUID client_id;
    @NotNull(message = "user_id cannot be null")
    private UUID user_id;
    @NotNull(message = "response_type cannot be null")
    private String redirect_uri;
    @NotNull(message = "you should write at least one scope")
    private String scopes;
}
