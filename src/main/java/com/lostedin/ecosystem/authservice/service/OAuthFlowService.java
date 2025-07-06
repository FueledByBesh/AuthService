package com.lostedin.ecosystem.authservice.service;

import com.lostedin.ecosystem.authservice.dto.oauthflow.AuthorizeDTO;
import com.lostedin.ecosystem.authservice.dto.session.ReadOnlyPreSessionDTO;
import com.lostedin.ecosystem.authservice.entity.PreSessionEntity;
import com.lostedin.ecosystem.authservice.enums.OAuthFlowParameterTypes.CodeChallengeMethodType;
import com.lostedin.ecosystem.authservice.enums.OAuthFlowParameterTypes.OAuthResponseType;
import com.lostedin.ecosystem.authservice.exception.ServiceException;
import com.lostedin.ecosystem.authservice.exception.UnknownException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthFlowService {

    // TODO: All logic in AuthController should be written here

    private final OAuthClientService clientService;
    private final SessionService sessionService;


    //returns PreSession Id
    public UUID startAuthorizationFlow(AuthorizeDTO authorizeDTO) {

        UUID clientId = clientService.validateClient(authorizeDTO.getClientId());
        if (clientId == null) {
            throw new ServiceException(400, "Invalid client id");
        }

        // TODO: Check which type of client is this
        if (clientService.isPublicClient(clientId)) {
            if (authorizeDTO.getCodeChallenge() == null || authorizeDTO.getCodeChallengeMethod() == null) {
                throw new ServiceException(400, "code_challenge and code_challenge_method are required for public clients");
            }

            if (authorizeDTO.getCodeChallengeMethod() == CodeChallengeMethodType.PLAIN) {
                throw new ServiceException(400, "please use another encrypt method, plain is not allowed");
            }

        } else if (!clientService.isConfidentialClient(clientId)) {
            throw new ServiceException(400, "Invalid client type");
        }

        // TODO: Create PreSession

        return sessionService
                .createPreSession(clientId,
                        authorizeDTO.getRedirectURI(),
                        authorizeDTO.getScope(),
                        authorizeDTO.getState(),
                        authorizeDTO.getResponseType(),
                        authorizeDTO.getCodeChallenge(),
                        authorizeDTO.getCodeChallengeMethod());
    }

    // Returns URI
    public String handleAuthorizationCallback(UUID preSessionId) {

        ReadOnlyPreSessionDTO preSession = sessionService.getPreSessionDTO(preSessionId);

        if (preSession.getResponseType() != OAuthResponseType.CODE) {
            log.error("Unexpected Error: Mapper couldn't get response type from PreSessionEntity");
            throw new UnknownException("Internal Server Error: Invalid response type for presession");
        }

        String code = sessionService.setPreSessionCode(preSessionId);

        return UriComponentsBuilder.fromUriString(preSession.getRedirectURI())
                .queryParam("code", code)
                .queryParam("state", preSession.getState())
                .build().toString();
    }

    public String handleAuthorizationCallbackAccessDenied(UUID preSessionId) {

        ReadOnlyPreSessionDTO preSession = sessionService.getPreSessionDTO(preSessionId);

        return UriComponentsBuilder.fromUriString(preSession.getRedirectURI())
                .queryParam("error", "access_denied")
                .queryParam("error_description", "The resource owner denied the request.")
                .queryParam("state", preSession.getState())
                .build().toString();
    }


    public void authenticate() {
        // TODO: Not Implemented
    }

    private void validateParameters(AuthorizeDTO authorizeDTO) {
    }

}
