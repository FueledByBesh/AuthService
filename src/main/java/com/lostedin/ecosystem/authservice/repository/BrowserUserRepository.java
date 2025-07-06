package com.lostedin.ecosystem.authservice.repository;

import com.lostedin.ecosystem.authservice.entity.BrowserUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BrowserUserRepository extends JpaRepository<BrowserUserEntity, UUID> {

    @Query("SELECT bs.userId FROM BrowserUserEntity bs WHERE bs.browserId = :browserId")
    List<UUID> getUsersIdByBrowserId(UUID browserId);



}
