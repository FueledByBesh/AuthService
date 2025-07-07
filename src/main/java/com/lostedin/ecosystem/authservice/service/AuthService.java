package com.lostedin.ecosystem.authservice.service;

import com.lostedin.ecosystem.authservice.dto.oauthflow.AuthorizeDTO;
import com.lostedin.ecosystem.authservice.dto.user.UserLoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SessionService sessionService;
    private final UserService userService;


    public boolean authenticateUser(UserLoginDTO user, UUID preSessionId) {

        Optional<UUID> userId = userService.validateUser(user.getEmail(), user.getPassword());
        if(userId.isEmpty()) {
            return false;
        }

        sessionService.setPreSessionUser(preSessionId, userId.get());
        return true;
    }

}
