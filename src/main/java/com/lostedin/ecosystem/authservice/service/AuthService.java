package com.lostedin.ecosystem.authservice.service;

import com.lostedin.ecosystem.authservice.dto.oauthflow.AuthorizeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    // TODO: All logic in AuthController should be written here

    private final SessionService sessionService;

    public void startAuthorizationFlow(AuthorizeDTO authorizeDTO){

        // TODO: Check which type of client is this

        // TODO: Create PreSession



    }


    public void authenticate(){
        // TODO: Not Implemented
    }
}
