package com.lostedin.ecosystem.authservice.dto.session;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lostedin.ecosystem.authservice.enums.OAuthResponseType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreSessionCreateDTO {

    private UUID preSessionId;
    private UUID clientId;
    private String state;
    private String redirectUri;
    private OAuthResponseType responseType;
    private String scopes;

}
