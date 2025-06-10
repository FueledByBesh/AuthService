package com.lostedin.ecosystem.authservice.repository;

import com.lostedin.ecosystem.authservice.entity.OAuthClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OAuthClientRepository extends JpaRepository<OAuthClientEntity, UUID> {

}
