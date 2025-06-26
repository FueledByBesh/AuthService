package com.lostedin.ecosystem.authservice.mapper;

import com.lostedin.ecosystem.authservice.dto.session.PreSessionCreateDTO;
import com.lostedin.ecosystem.authservice.dto.session.SessionCreateDTO;
import com.lostedin.ecosystem.authservice.entity.PreSessionEntity;
import com.lostedin.ecosystem.authservice.entity.SessionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionDtoEntityMapper {

    SessionEntity sessionDtoToEntity(SessionCreateDTO dto);
    PreSessionEntity presessionDtoToEntity(PreSessionCreateDTO dto);



}
