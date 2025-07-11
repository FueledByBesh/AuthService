package com.lostedin.ecosystem.authservice.dto.session;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lostedin.ecosystem.authservice.enums.OAuthFlowParameterTypes.CodeChallengeMethodType;
import com.lostedin.ecosystem.authservice.enums.OAuthFlowParameterTypes.OAuthResponseType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreSessionCreateDTO {

    @NotNull(message = "client_id cannot be null")
    private UUID clientId;
    private String state;
    @NotNull(message = "redirect_uri cannot be null")
    private String redirectURI;
    @NotNull(message = "response_type cannot be null")
    private OAuthResponseType responseType;
    private String scopes;
    private String codeChallenge;
    private CodeChallengeMethodType codeChallengeMethod;

}
