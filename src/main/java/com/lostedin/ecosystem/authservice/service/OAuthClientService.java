package com.lostedin.ecosystem.authservice.service;

import com.lostedin.ecosystem.authservice.entity.OAuthClientEntity;
import com.lostedin.ecosystem.authservice.repository.OAuthClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthClientService {

    private final UserService userService;
    private final OAuthClientRepository clientRepository;


    public boolean createOAuthClient() {
        //TODO: Not Implemented
        return false;
    }

    public boolean validateClient(UUID clientId){
        //TODO: Not Implemented
        return false;
    }

    public UUID validateClient(String clientId){
        //TODO: Not Implemented
        // 1) should return client id in UUID format
        // 2) should return null if client id is not valid or client is not found
        return null;
    }

    public boolean validateClient(UUID clientId, String clientSecret){
        //TODO: Not Implemented
        return false;
    }

    public Optional<OAuthClientEntity> getOAuthClientIdUUID(UUID clientId){
        return clientRepository.findById(clientId);
    }

//    public OAuthClientEntity getOAuthClientIdString(String clientId){}



}
