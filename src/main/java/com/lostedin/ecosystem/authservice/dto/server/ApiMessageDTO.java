package com.lostedin.ecosystem.authservice.dto.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiMessageDTO {
    private int status;
    private String message;
}
