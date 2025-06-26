package com.lostedin.ecosystem.authservice.dto.oauthclient;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OAuthClientCredentialsDTO {
    private String client_id;
    private String client_secret;
}
