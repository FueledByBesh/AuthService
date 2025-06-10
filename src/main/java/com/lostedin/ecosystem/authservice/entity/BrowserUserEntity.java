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
    private UUID browser_id;
    private UUID user_id;
    private Instant first_logged_at;
    private Instant last_logged_at;

}
