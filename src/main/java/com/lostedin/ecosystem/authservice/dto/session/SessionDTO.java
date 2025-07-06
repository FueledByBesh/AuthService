package com.lostedin.ecosystem.authservice.dto.session;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionDTO {

    @NotNull(message = "session_id cannot be null")
    private UUID client_id;

    private UUID user_id;
    @NotNull(message = "response_type cannot be null")
    private String redirect_uri;
    @NotNull(message = "you should write at least one scope")
    private String scopes;
    private Instant expires_at;
    private Instant created_at;

}
