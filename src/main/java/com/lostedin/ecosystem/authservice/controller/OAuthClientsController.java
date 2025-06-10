package com.lostedin.ecosystem.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/v1/clients")
public class OAuthClientsController {

    // TODO: Not Implemented

    @PostMapping("/create")
    public ResponseEntity<String> create(){
        return null;
    }


    @PostMapping("/delete")
    public ResponseEntity<String> delete(){
        return null;
    }


}
