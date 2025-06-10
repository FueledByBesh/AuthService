package com.lostedin.ecosystem.authservice.repository;

import com.lostedin.ecosystem.authservice.entity.PreSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PreSessionRepository extends JpaRepository<PreSessionEntity, UUID> {

    Optional<PreSessionEntity> findByPre_session_id(UUID preSessionId);


}
