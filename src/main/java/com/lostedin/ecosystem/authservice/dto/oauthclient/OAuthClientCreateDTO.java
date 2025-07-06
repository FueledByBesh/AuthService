package com.lostedin.ecosystem.authservice.dto.oauthclient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lostedin.ecosystem.authservice.enums.OAuthClientType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OAuthClientCreateDTO {

    @NotNull(message = "app_name cannot be null")
    private String appName;
    @NotNull(message = "you should write at least one uri")
    private Set<String> redirectURIs;
    @NotNull(message = "client_type cannot be null")
    private OAuthClientType clientType;

}
