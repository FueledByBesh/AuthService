package com.lostedin.ecosystem.authservice.dto.oauthflow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lostedin.ecosystem.authservice.enums.OAuthFlowParameterTypes.CodeChallengeMethodType;
import com.lostedin.ecosystem.authservice.enums.OAuthFlowParameterTypes.OAuthResponseType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;


@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizeDTO {

    @NotNull(message = "client_id cannot be null")
    private String clientId;
    @NotNull(message = "redirect_uri cannot be null")
    private String redirectURI;
    @NotNull(message = "response_type cannot be null")
    private OAuthResponseType responseType;
    @NotNull(message = "scope cannot be null")
    private String scope;
    private String state;
    private String codeChallenge;
    private CodeChallengeMethodType codeChallengeMethod;


}
