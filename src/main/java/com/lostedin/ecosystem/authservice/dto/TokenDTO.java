package com.lostedin.ecosystem.authservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
}
