package com.lostedin.ecosystem.authservice.service;

import com.lostedin.ecosystem.authservice.dto.session.PreSessionCreateDTO;
import com.lostedin.ecosystem.authservice.dto.session.ReadOnlyPreSessionDTO;
import com.lostedin.ecosystem.authservice.dto.session.SessionCreateDTO;
import com.lostedin.ecosystem.authservice.entity.PreSessionEntity;
import com.lostedin.ecosystem.authservice.entity.SessionEntity;
import com.lostedin.ecosystem.authservice.enums.OAuthFlowParameterTypes.CodeChallengeMethodType;
import com.lostedin.ecosystem.authservice.enums.OAuthFlowParameterTypes.OAuthResponseType;
import com.lostedin.ecosystem.authservice.exception.ServiceException;
import com.lostedin.ecosystem.authservice.exception.UnknownException;
import com.lostedin.ecosystem.authservice.mapper.SessionDtoEntityMapper;
import com.lostedin.ecosystem.authservice.model.DtoValidator;
import com.lostedin.ecosystem.authservice.model.Helper;
import com.lostedin.ecosystem.authservice.repository.PreSessionRepository;
import com.lostedin.ecosystem.authservice.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    private final SessionRepository sessionRepository;
    private final PreSessionRepository preSessionRepository;


    private final OAuthClientService clientService;
    private final TokenService tokenService;
    private final SessionDtoEntityMapper sessionMapper;
    private final int AUTH_CODE_EXPIRE_TIME_MILLIS = 5 * 60 * 1000; // 5 minutes

    @Transactional
    public void createSession(SessionCreateDTO session) {

        try {
            DtoValidator.validateOrThrow(session);
        } catch (IllegalArgumentException e) {
            log.error("Invalid session DTO", e);
            throw new ServiceException(500, "Internal Server Error: Smth Get Wrong");
        }
        SessionEntity sessionEntity = sessionMapper.sessionDtoToEntity(session);
        sessionEntity = sessionRepository.save(sessionEntity);
        tokenService.createRefreshToken(sessionEntity);

        /* TODO: 05.07.2025 Not Finished
            Ideas at this point:
            1) Надо обдумать создать ли сессию сразу после передачи AuthCode клиенту или же
            после валидации AuthCode. Если до валидации, то надо будет создать еще один StatusType
            (типо NotStarted or smth else)
            2) Думаю создание refresh токена для сессии надо вывести в отдельный метод,
            потому что refresh токена для сессии можно генерировать и после создания сессии, то есть
            что бы продолжить сессию
         */

    }

    public UUID createPreSession(UUID clientId,
                                 String redirectUri,
                                 String scopes, String state,
                                 OAuthResponseType responseType,
                                 String codeChallenge,
                                 CodeChallengeMethodType codeChallengeMethod) {

        // checking whether a client exists
        if (clientId == null) {
            throw new ServiceException(400, "Invalid client id");
        }

        // Generating a presession
        PreSessionCreateDTO preSession = PreSessionCreateDTO.builder()
                .clientId(clientId)
                .redirectURI(redirectUri)
                .scopes(scopes)
                .state(state)
                .responseType(responseType)
                .codeChallenge(codeChallenge)
                .codeChallengeMethod(codeChallengeMethod)
                .build();


        PreSessionEntity preSessionEntity = sessionMapper.presessionDtoToEntity(preSession);
        try {
            return preSessionRepository.saveAndFlush(preSessionEntity).getPreSessionId();
        } catch (Exception e) {
            log.error("Error while saving PreSession in db", e);
            throw new ServiceException(500, "Smth went wrong, couldn't create presession");
        }
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

    // Returns Authorization Code
    public String setPreSessionCode(UUID presessionId) {
        PreSessionEntity preSession = validatePreSession(presessionId);
        String code = generateAuthCode();
        preSession = setPreSessionCode(preSession, code);
        log.info("Authorization code generated for presession {} and code expires at {}", preSession.getPreSessionId(), preSession.getCodeExpiresAt());
        return preSession.getAuthCode();
    }

    public void validateAuthCode(String code, UUID preSessionId) {

        PreSessionEntity preSessionEntity = validatePreSession(preSessionId);
        if (preSessionEntity.getCodeExpiresAt().isBefore(Instant.now())) {
            throw new ServiceException(400, "Authorization code has expired");
        }
        if (!preSessionEntity.getAuthCode().equals(code)) {
            throw new ServiceException(400, "Invalid authorization code");
        }

    }

    public ReadOnlyPreSessionDTO getPreSessionDTO(UUID preSessionId) {
        PreSessionEntity preSessionEntity = validatePreSession(preSessionId);
        return sessionMapper.preSessionEntityToReadOnlyPreSessionDto(preSessionEntity);
    }

    public void refreshAuthCode(String code, UUID preSessionId) {
    }

    private PreSessionEntity setPreSessionUser(PreSessionEntity preSession, UUID userId) {

        try {
            preSession.setUserId(userId);
            log.info("PreSession user set to {} for presession {}", userId, preSession.getPreSessionId());
            return preSessionRepository.saveAndFlush(preSession);
        } catch (Exception e) {
            log.error("Error while setting PreSession user for preSession: {}",preSession.getPreSessionId() ,e);
            throw new ServiceException(500, "Smth went wrong, couldn't set user for presession");
        }
    }

    private PreSessionEntity setPreSessionCode(PreSessionEntity preSession, String code) {
        try {
            preSession.setAuthCode(code);
            preSession.setCodeExpiresAt(Instant.now().plusMillis(AUTH_CODE_EXPIRE_TIME_MILLIS));
            log.info("Authorization code set for presession {}", preSession.getPreSessionId());
            return preSessionRepository.saveAndFlush(preSession);
        } catch (Exception e) {
            log.error("Error while setting Authorization code for PreSession: {}",preSession.getPreSessionId(), e);
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

    private PreSessionEntity validatePreSession(UUID preSessionId) throws UnknownException {
        return preSessionRepository.findByPreSessionId(preSessionId)
                .orElseThrow(() -> new UnknownException("Smth went wrong, presession not found"));
    }

    private String generateAuthCode() {
        return Helper.generateRandomBase64UrlEncodedString(32);
    }


}
