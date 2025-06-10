package com.lostedin.ecosystem.authservice.imitation;

import com.lostedin.ecosystem.authservice.dto.User.UserDTO;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
public class UserImitation implements Serializable {
    private UUID id;
    private String username;
    private String email;
    private String password;

    public UserDTO getDTO(){
        return UserDTO.builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .build();
    }

    public UUID getId() {
        if(id == null) {
            id = generateRadomUUID();
        }
        return id;
    }

    private UUID generateRadomUUID() {
        return UUID.randomUUID();
    }
}
