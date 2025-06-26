package com.lostedin.ecosystem.authservice.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterDTO {

    private String nickname;
    private String email;
    private String password;
    private String confirmPassword;
    private Boolean isEmailVerified;

}
