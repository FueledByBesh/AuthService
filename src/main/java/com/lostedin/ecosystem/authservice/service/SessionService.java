package com.lostedin.ecosystem.authservice.service;

import com.lostedin.ecosystem.authservice.dto.session.PreSessionCreateDTO;
import com.lostedin.ecosystem.authservice.dto.session.SessionCreateDTO;
import com.lostedin.ecosystem.authservice.entity.PreSessionEntity;
import com.lostedin.ecosystem.authservice.entity.SessionEntity;
import com.lostedin.ecosystem.authservice.enums.OAuthResponseType;
import com.lostedin.ecosystem.authservice.exception.ServiceException;
import com.lostedin.ecosystem.authservice.mapper.SessionDtoEntityMapper;
import com.lostedin.ecosystem.authservice.model.DtoValidator;
import com.lostedin.ecosystem.authservice.model.Helper;
import com.lostedin.ecosystem.authservice.repository.PreSessionRepository;
import com.lostedin.ecosystem.authservice.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final PreSessionRepository preSessionRepository;


    private final OAuthClientService clientService;
    private final SessionDtoEntityMapper sessionMapper;
    private final int AUTH_CODE_EXPIRE_TIME_MILLIS = 5 * 60 * 1000;

    public boolean createSession(SessionCreateDTO session) {

        DtoValidator.validateOrThrow(session);
        SessionEntity sessionEntity = sessionMapper.sessionDtoToEntity(session);


        return false;
    }

    public boolean createPreSession(PreSessionCreateDTO session) {


        return false;
    }

    public PreSessionCreateDTO generatePreSession(String clientId, String redirectUri, String scopes, String state, String responseType) {

        // generating a presession id
        UUID preSessionId = UUID.randomUUID();
        if (preSessionRepository.findByPre_session_id(preSessionId).isPresent()) {
            // TODO: вывести в log что id уже существует
            System.out.println("PreSession with id " + preSessionId + " already exists, generating new one");
            return generatePreSession(clientId, redirectUri, scopes, state, responseType);
        }

        // checking whether a client exists
        UUID client_id = clientService.validateClient(clientId);
        if (client_id == null) {
            throw new ServiceException(400, "Invalid client id");
        }

        OAuthResponseType oAuthResponseType;
        try {
            oAuthResponseType = Helper.getOauthResponseType(responseType);
        } catch (IllegalArgumentException e) {
            throw new ServiceException(400, "Error: " + e.getMessage());
        }

        // Generating a presession
        PreSessionCreateDTO preSession = PreSessionCreateDTO.builder()
                .pre_session_id(preSessionId)
                .client_id(client_id)
                .redirect_uri(redirectUri)
                .scopes(scopes)
                .state(state)
                .responseType(oAuthResponseType)
                .build();

        PreSessionEntity preSessionEntity = sessionMapper.presessionDtoToEntity(preSession);
        preSessionRepository.saveAndFlush(preSessionEntity);

        return preSession;
    }


//    public void setPreSessionUserAndGenerateCode(UUID preSessionId, UUID userId) {
//
//
//        try {
//            PreSessionEntity preSessionEntity = validatePreSession(preSessionId);
//            setPreSessionUser(preSessionEntity, userId);
//            setPreSessionCode(preSessionEntity, code);
//
//            preSessionRepository.saveAndFlush(preSessionEntity);
//        } catch (Exception e) {
//            throw new ServiceException(500, "Smth went wrong, couldn't set auth code for presession");
//        }
//    }

    public void setPreSessionUser(UUID presessionId, UUID userId) {
        PreSessionEntity preSession = validatePreSession(presessionId);
        setPreSessionUser(preSession, userId);
    }

    public void validateAuthCode(String code, UUID preSessionId) {

        PreSessionEntity preSessionEntity = validatePreSession(preSessionId);
        if (preSessionEntity.getCode_expires_at().isBefore(Instant.now())) {
            throw new ServiceException(400, "Authorization code has expired");
        }
        if (!preSessionEntity.getAuth_code().equals(code)) {
            throw new ServiceException(400, "Invalid authorization code");
        }

    }

    public void refreshAuthCode(String code, UUID preSessionId) {
    }

    private void setPreSessionUser(PreSessionEntity preSession, UUID userId) {

        preSession.setUser_id(userId);
        try {
            // TODO: Log this text
            System.out.println("PreSession user set to " + userId.toString() + " for presession "
                    + preSession.getPre_session_id().toString());
        } catch (Exception e) {
            throw new ServiceException(500, "Smth went wrong, couldn't set user for presession");
        }
    }

    private void setPreSessionCode(PreSessionEntity preSession, String code) {
        preSession.setAuth_code(code);
        preSession.setCode_expires_at(Instant.now().plusMillis(AUTH_CODE_EXPIRE_TIME_MILLIS));
        try {
            // TODO: Log this text
            System.out.println("Authorization code: " + code + " for presession "
                    + preSession.getPre_session_id().toString());
        } catch (Exception e) {
            throw new ServiceException(500, "Smth went wrong, couldn't set auth code for presession");
        }
    }

    public void deletePreSession(UUID preSessionId) {
        // TODO: Not Implemented
        try {
            preSessionRepository.deleteById(preSessionId);
        } catch (Exception e) {
            throw new ServiceException(500, "Error: smth went wrong while deleting presession");
        }
    }

    private PreSessionEntity validatePreSession(UUID preSessionId) {
        return preSessionRepository.findByPre_session_id(preSessionId)
                .orElseThrow(() -> new ServiceException(500, "Smth went wrong, presession not found"));
    }

    private String generateAuthCode() {
        return Helper.generateCode();
    }


}
