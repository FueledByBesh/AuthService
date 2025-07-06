package com.lostedin.ecosystem.authservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "browser_user")
public class BrowserUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "browser_id")
    private UUID browserId;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "first_logged_at")
    private Instant firstLoggedAt;
    @Column(name = "last_logged_at")
    private Instant lastLoggedAt;

}
