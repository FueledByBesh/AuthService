package com.lostedin.ecosystem.authservice.service;

import com.lostedin.ecosystem.authservice.dto.oauthclient.OAuthClientCreateDTO;
import com.lostedin.ecosystem.authservice.dto.oauthclient.OAuthClientCredentialsDTO;
import com.lostedin.ecosystem.authservice.entity.OAuthClientEntity;
import com.lostedin.ecosystem.authservice.entity.OAuthClientURIsEntity;
import com.lostedin.ecosystem.authservice.exception.ServiceException;
import com.lostedin.ecosystem.authservice.mapper.OAuthClientDtoEntityMapper;
import com.lostedin.ecosystem.authservice.model.DtoValidator;
import com.lostedin.ecosystem.authservice.model.Helper;
import com.lostedin.ecosystem.authservice.repository.OAuthClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthClientService {

    private final UserService userService;
    private final OAuthClientRepository clientRepository;
    private final OAuthClientDtoEntityMapper mapper;


    @Transactional
    public OAuthClientCredentialsDTO createOAuthClient(OAuthClientCreateDTO client) {

        DtoValidator.validateOrThrow400Exception(client);

        OAuthClientEntity clientEntity = mapper.createDtoToClientEntity(client);
        clientEntity.setClient_secret(generateClientSecret());

        List<OAuthClientURIsEntity> uriList = new ArrayList<>();
        client.getRedirectURIs().forEach(redirectUri -> {
            OAuthClientURIsEntity uriEntity = new OAuthClientURIsEntity();
            uriEntity.setClient(clientEntity);
            uriEntity.setUri(redirectUri);
            uriList.add(uriEntity);
        });
        clientEntity.setUris(uriList);

        try {
            return mapper.clientEntityToCredentialDto(clientRepository.saveAndFlush(clientEntity));
        }catch (Exception e){
            throw new ServiceException(500, "Failed to create OAuth client: " + e.getMessage());
        }
    }

    public void deleteOAuthClient(UUID clientId) {
        clientRepository.deleteById(clientId);
    }

    private OAuthClientEntity validateClient(UUID clientId){
        return clientRepository.findById(clientId).orElse(null);
    }

    public UUID validateClient(String clientId){
        //TODO: Partially Implemented
        // 1) should return client id in UUID format
        // 2) should return null if client id is not valid or client is not found
        UUID client_id;
        try {
            client_id = UUID.fromString(clientId);
        }catch (IllegalArgumentException e){
            throw new ServiceException(400, "Invalid client id: " + clientId);
        }

        OAuthClientEntity client = validateClient(client_id);
        if(client == null) {
            throw new ServiceException(400, "Invalid client id: " + clientId);
        }

        return client_id;
    }

    public boolean validateClient(UUID clientId, String clientSecret){
        //TODO: Not Implemented

        OAuthClientEntity client = validateClient(clientId);

        if(client == null) {
            throw new ServiceException(400, "Invalid client id: " + clientId);
        }

        return client.getClient_secret().equals(clientSecret);
    }


//    public OAuthClientEntity getOAuthClientIdString(String clientId){}



    private String generateClientSecret(){
        String secret = Helper.generateSecret();
        if(secret.length() != 64) {
            throw new ServiceException(500, "Error generating secret");
        }
        return secret;
    }

}
