package com.lostedin.ecosystem.authservice.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginDTO {

    private String email;
    private String password;

}
