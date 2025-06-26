package com.lostedin.ecosystem.authservice.dto.oauthclient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lostedin.ecosystem.authservice.enums.OAuthClientAccessType;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OAuthClientCreateDTO {

    private String app_name;
    private Set<String> redirectURIs;
    private OAuthClientAccessType access_type;

}
