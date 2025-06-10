package com.lostedin.ecosystem.authservice.service;

import com.lostedin.ecosystem.authservice.dto.User.UserMinDataDTO;
import com.lostedin.ecosystem.authservice.dto.session.PreSessionDTO;
import com.lostedin.ecosystem.authservice.dto.session.SessionDTO;
import com.lostedin.ecosystem.authservice.entity.PreSessionEntity;
import com.lostedin.ecosystem.authservice.enums.OAuthResponseType;
import com.lostedin.ecosystem.authservice.exception.ServiceException;
import com.lostedin.ecosystem.authservice.model.Helper;
import com.lostedin.ecosystem.authservice.repository.BrowserUserRepository;
import com.lostedin.ecosystem.authservice.repository.PreSessionRepository;
import com.lostedin.ecosystem.authservice.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final PreSessionRepository preSessionRepository;

//    private final BrowserService browserService;
    private final OAuthClientService clientService;
//    private final UserService userService;

    public boolean createSession(SessionDTO session) {


        return false;
    }

    public boolean createPreSession(PreSessionDTO session) {



        return false;
    }

    public PreSessionDTO generatePreSession(String clientId, String redirectUri, String scopes, String state,String responseType) {

        // generating a presession id
        UUID preSessionId = UUID.randomUUID();
        if(preSessionRepository.findByPre_session_id(preSessionId).isPresent()) {
            // TODO: вывести в log что id уже существует
            System.out.println("PreSession with id " + preSessionId + " already exists, generating new one");
            return generatePreSession(clientId, redirectUri, scopes, state, responseType);
        }

        // checking whether a client exists
        UUID client_id = clientService.validateClient(clientId);
        if(client_id == null) {
            throw new ServiceException(400, "Invalid client id");
        }

        OAuthResponseType oAuthResponseType;
        try {
            oAuthResponseType = Helper.getOauthResponseType(responseType);
        }catch (IllegalArgumentException e) {
            throw new ServiceException(400, "Error: "+e.getMessage());
        }


        // Generating a presession
        PreSessionDTO preSession = PreSessionDTO.builder()
                .pre_session_id(preSessionId)
                .client_id(client_id)
                .redirect_uri(redirectUri)
                .scopes(scopes)
                .state(state)
                .responseType(oAuthResponseType)
                .created_at(Instant.now())
                .build();

        // TODO: get entity from DTO using Mapper (status: not implemented)
        PreSessionEntity preSessionEntity = new PreSessionEntity();
        preSessionRepository.save(preSessionEntity);

        return preSession;
    }

    public boolean setPreSessionUser(UUID preSessionId, UUID userId) {
        PreSessionEntity preSessionEntity = preSessionRepository.findByPre_session_id(preSessionId)
                        .orElseThrow(()-> new ServiceException(500, "Smth went wrong, presession not found"));
        preSessionEntity.setUser_id(userId);
        try {
            preSessionRepository.saveAndFlush(preSessionEntity);
        }catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean deletePreSession(UUID preSessionId) {
        // TODO: Not Implemented
        return false;
    }



}
