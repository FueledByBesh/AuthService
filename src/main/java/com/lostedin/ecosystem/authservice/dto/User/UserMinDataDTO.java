package com.lostedin.ecosystem.authservice.dto.User;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMinDataDTO {

    private String username;
    private String email;
    private String avatarUrl;

}
