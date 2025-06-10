package com.lostedin.ecosystem.authservice.dto.User;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginDTO {

    private String email;
    private String password;

}
