package com.lostedin.ecosystem.authservice.mapper;

import com.lostedin.ecosystem.authservice.dto.oauthclient.OAuthClientCreateDTO;
import com.lostedin.ecosystem.authservice.dto.oauthclient.OAuthClientCredentialsDTO;
import com.lostedin.ecosystem.authservice.entity.OAuthClientEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OAuthClientDtoEntityMapper {

    OAuthClientEntity createDtoToClientEntity(OAuthClientCreateDTO dto);
    OAuthClientCredentialsDTO clientEntityToCredentialDto(OAuthClientEntity entity);
}
