package com.lostedin.ecosystem.authservice.controller;

import com.lostedin.ecosystem.authservice.dto.oauthclient.OAuthClientCreateDTO;
import com.lostedin.ecosystem.authservice.dto.oauthclient.OAuthClientCredentialsDTO;
import com.lostedin.ecosystem.authservice.service.OAuthClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/v1/clients")
public class OAuthClientsController {

    private final OAuthClientService clientService;


    @PostMapping("/create")
    public ResponseEntity<OAuthClientCredentialsDTO> create(@RequestBody OAuthClientCreateDTO client){
        // TODO: Should change logic of creating clients, Its just for test

        OAuthClientCredentialsDTO credentialsDTO = clientService.createOAuthClient(client);
        return ResponseEntity.ok(credentialsDTO);
    }


    @PostMapping("/delete")
    public ResponseEntity<String> delete(){
        // TODO: Not Implemented
        return null;
    }

    @PostMapping("/regenerate-secret")
    public ResponseEntity<String> update(){
        // TODO: Not Implemented
        return null;
    }

}
