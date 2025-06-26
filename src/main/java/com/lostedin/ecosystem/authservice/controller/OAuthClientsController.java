package com.lostedin.ecosystem.authservice.controller;

import com.lostedin.ecosystem.authservice.dto.oauthclient.OAuthClientCreateDTO;
import com.lostedin.ecosystem.authservice.service.OAuthClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/v1/clients")
public class OAuthClientsController {

    private final OAuthClientService clientService;

    // TODO: Not Implemented

    @PostMapping("/create")
    public ResponseEntity<String> create(OAuthClientCreateDTO client){



        return null;
    }


    @PostMapping("/delete")
    public ResponseEntity<String> delete(){
        return null;
    }

    @PostMapping("/regenerate-secret")
    public ResponseEntity<String> update(){
        return null;
    }

}
