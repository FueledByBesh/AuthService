package com.lostedin.ecosystem.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/v1")
public class OAuthController {

    @GetMapping("/auth")
    protected String authorize(
            @RequestParam String client_id,
            @RequestParam String redirect_uri,
            @RequestParam String response_type,
            @RequestParam String scope,
            @RequestParam String state){
        return "authorized";
    }

    @PostMapping("/token")
    protected ResponseEntity<String> token(String body){
        return ResponseEntity.ok("token");
    }

    @PostMapping("/login")
    protected ResponseEntity<String> login(@RequestBody String body){
        return ResponseEntity.ok("logged in");
    }

    @PostMapping("/logout")
    protected ResponseEntity<String> logout(@RequestBody String body){
        return ResponseEntity.ok("logged out");
    }

    @PostMapping("/register")
    protected ResponseEntity<String> register(@RequestBody String body){
        return ResponseEntity.ok("registered");
    }


}
