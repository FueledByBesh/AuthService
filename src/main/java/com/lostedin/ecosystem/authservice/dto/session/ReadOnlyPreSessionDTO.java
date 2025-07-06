package com.lostedin.ecosystem.authservice.dto.session;

import com.lostedin.ecosystem.authservice.enums.OAuthFlowParameterTypes.CodeChallengeMethodType;
import com.lostedin.ecosystem.authservice.enums.OAuthFlowParameterTypes.OAuthResponseType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class ReadOnlyPreSessionDTO {
    private UUID clientId;
    private String state;
    private String redirectURI;
    private String scopes;
    private OAuthResponseType responseType;
    private Instant createdAt;
    private UUID userId;
    private String authCode;
    private Instant codeExpiresAt;
    private String codeChallenge;
    private CodeChallengeMethodType codeChallengeMethodType;
}
