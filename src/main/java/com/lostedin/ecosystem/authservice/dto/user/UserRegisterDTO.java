package com.lostedin.ecosystem.authservice.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterDTO {

    // TODO: 07.07.2025 Status: Not Implemented
    //  Should write minimal dto validation

    @NotNull(message = "username cannot be null")
    private String nickname;
    private String email;
    private String password;
    private String confirmPassword;
    private Boolean isEmailVerified;

}
