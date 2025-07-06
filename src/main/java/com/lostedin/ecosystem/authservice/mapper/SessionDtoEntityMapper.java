package com.lostedin.ecosystem.authservice.mapper;

import com.lostedin.ecosystem.authservice.dto.session.PreSessionCreateDTO;
import com.lostedin.ecosystem.authservice.dto.session.ReadOnlyPreSessionDTO;
import com.lostedin.ecosystem.authservice.dto.session.SessionCreateDTO;
import com.lostedin.ecosystem.authservice.entity.PreSessionEntity;
import com.lostedin.ecosystem.authservice.entity.SessionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SessionDtoEntityMapper {

    @Mapping(target = "clientId" , source = "client_id")
    @Mapping(target = "redirectURI" , source = "redirect_uri")
    @Mapping(target = "userId" , source = "user_id")
    SessionEntity sessionDtoToEntity(SessionCreateDTO dto);
    PreSessionEntity presessionDtoToEntity(PreSessionCreateDTO dto);

    ReadOnlyPreSessionDTO preSessionEntityToReadOnlyPreSessionDto(PreSessionEntity entity);

}
