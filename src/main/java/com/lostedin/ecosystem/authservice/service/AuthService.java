package com.lostedin.ecosystem.authservice.service;

import com.lostedin.ecosystem.authservice.dto.oauthflow.AuthorizeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {



    private final SessionService sessionService;

}
