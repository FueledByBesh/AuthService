package com.lostedin.ecosystem.authservice.service;

import com.lostedin.ecosystem.authservice.dto.user.UserMinDataDTO;
import com.lostedin.ecosystem.authservice.dto.user.UserRegisterDTO;
import org.springframework.http.MediaType;
import com.lostedin.ecosystem.authservice.dto.server.ApiMessageDTO;
import com.lostedin.ecosystem.authservice.dto.user.UserDTO;
import com.lostedin.ecosystem.authservice.exception.NotFoundException;
import com.lostedin.ecosystem.authservice.exception.WebClientExeption;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;


@Service
public class UserService {

    private final WebClient userServiceWebClient;

    public UserService(@Qualifier("userServiceWebClient") WebClient webClient){
        this.userServiceWebClient = webClient;
    }

    public List<UserMinDataDTO> getUsersMinDataByUserId(List<UUID> userIds){
        //TODO: Not Implemented
        return null;
    }

    public Optional<UUID> validateUser(String email, String password){
        //TODO: Not Implemented
        // (Should return user id in UUID format wrapped into Optional)
        return Optional.empty();
    }

    /* TODO: изменить логику
        1) Надо вернуть DTO (пока сомнительно, надо подумать зачем)
        2) Вывести ошибку если null (тоже сомнительно, но лучше сделать это тут чем в контроллере)
        3) Описать что вернет в каких случаях как документация
        Status: Not Implemented (0:3)
     */
    public UUID createUser(UserRegisterDTO user){

        return userServiceWebClient.post()
                .uri("/api/v1/users/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(ApiMessageDTO.class)
                                .flatMap(errorBody ->
                                        Mono.error(new WebClientExeption(errorBody.getStatus(), "user Service error: " + errorBody.getMessage()))
                                )
                )
                .bodyToMono(UUID.class)
                .block();
    }


    @Deprecated
    public UserDTO createUser(UserDTO userDTO) {

        return userServiceWebClient.post()
                .uri("/api/v1/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(ApiMessageDTO.class)
                                .flatMap(errorBody ->
                                        Mono.error(new WebClientExeption(errorBody.getStatus(), "user Service error: " + errorBody.getMessage()))
                                )
                )
                .bodyToMono(UserDTO.class)
                .block();

    }

    @Deprecated
    public Optional<UserDTO> getUserById(UUID id) {

        return userServiceWebClient.get()
                .uri("/api/v1/users/id:{id}",id)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.NOT_FOUND,
                        response -> Mono.error(new NotFoundException("user not found"))
                )
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(ApiMessageDTO.class)
                                .switchIfEmpty(Mono.just(ApiMessageDTO.builder().message("Smth get wrong, Status unknown").status(500).build()))
                                .flatMap(errorBody ->
                                        Mono.error(new WebClientExeption(errorBody.getStatus(), "user Service error: " + errorBody.getMessage()))
                                )
                )
                .bodyToMono(UserDTO.class)
                .onErrorResume(NotFoundException.class, ex -> Mono.empty())
                .blockOptional();

    }

    @Deprecated
    public Optional<UserDTO> getUserByUsername(String username) {

        return userServiceWebClient.get()
                .uri("/api/v1/users/username:{username}",username)
                .retrieve()
                .onStatus(
                        status -> status==HttpStatus.NOT_FOUND,
                        clientResponse -> Mono.error(new NotFoundException("user not found"))
                )
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(ApiMessageDTO.class)
                                .switchIfEmpty(Mono.just(ApiMessageDTO.builder().message("Smth get wrong, Status unknown").status(500).build()))
                                .flatMap(errorBody ->
                                        Mono.error(new WebClientExeption(errorBody.getStatus(), "user Service error: " + errorBody.getMessage()))
                                )
                )
                .bodyToMono(UserDTO.class)
                .onErrorResume(NotFoundException.class, ex -> Mono.empty())
                .blockOptional();

    }

    @Deprecated
    public Optional<UserDTO> getUserByEmail(String email) {

        return userServiceWebClient.get()
                .uri("/api/v1/users/email:{email}",email)
                .retrieve()
                .onStatus(
                        status -> status==HttpStatus.NOT_FOUND,
                        clientResponse -> Mono.error(new NotFoundException("user not found"))
                )
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(ApiMessageDTO.class)
                                .switchIfEmpty(Mono.just(ApiMessageDTO.builder().message("Smth get wrong, Status unknown").status(500).build()))
                                .flatMap(errorBody ->
                                        Mono.error(new WebClientExeption(errorBody.getStatus(), "user Service error: " + errorBody.getMessage()))
                                )
                )
                .bodyToMono(UserDTO.class)
                .onErrorResume(NotFoundException.class, ex -> Mono.empty())
                .blockOptional();

    }

    public List<String> getAllUsers() {

        //TODO: Not Implemented
        return new ArrayList<>();
    }
}
