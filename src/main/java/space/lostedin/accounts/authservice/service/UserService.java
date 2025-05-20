package space.lostedin.accounts.authservice.service;

import org.springframework.http.MediaType;
import space.lostedin.accounts.authservice.dto.ApiMessageDTO;
import space.lostedin.accounts.authservice.dto.UserDTO;
import space.lostedin.accounts.authservice.exception.NotFoundException;
import space.lostedin.accounts.authservice.exception.WebClientExeption;
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

    public UserDTO createUser(UserDTO userDTO) {

        return userServiceWebClient.post()
                .uri("/api/v1/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(ApiMessageDTO.class)
                                .flatMap(errorBody ->
                                        Mono.error(new WebClientExeption(errorBody.getStatus(), "User Service error: " + errorBody.getMessage()))
                                )
                )
                .bodyToMono(UserDTO.class)
                .block();

    }

    public Optional<UserDTO> getUserById(UUID id) {

        return userServiceWebClient.get()
                .uri("/api/v1/user/id:{id}",id)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.NOT_FOUND,
                        response -> Mono.error(new NotFoundException("User not found"))
                )
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(ApiMessageDTO.class)
                                .switchIfEmpty(Mono.just(ApiMessageDTO.builder().message("Smth get wrong, Status unknown").status(500).build()))
                                .flatMap(errorBody ->
                                        Mono.error(new WebClientExeption(errorBody.getStatus(), "User Service error: " + errorBody.getMessage()))
                                )
                )
                .bodyToMono(UserDTO.class)
                .onErrorResume(NotFoundException.class, ex -> Mono.empty())
                .blockOptional();

    }

    public Optional<UserDTO> getUserByUsername(String username) {

        return userServiceWebClient.get()
                .uri("/api/v1/user/username:{username}",username)
                .retrieve()
                .onStatus(
                        status -> status==HttpStatus.NOT_FOUND,
                        clientResponse -> Mono.error(new NotFoundException("User not found"))
                )
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(ApiMessageDTO.class)
                                .switchIfEmpty(Mono.just(ApiMessageDTO.builder().message("Smth get wrong, Status unknown").status(500).build()))
                                .flatMap(errorBody ->
                                        Mono.error(new WebClientExeption(errorBody.getStatus(), "User Service error: " + errorBody.getMessage()))
                                )
                )
                .bodyToMono(UserDTO.class)
                .onErrorResume(NotFoundException.class, ex -> Mono.empty())
                .blockOptional();

    }

    public Optional<UserDTO> getUserByEmail(String email) {

        return userServiceWebClient.get()
                .uri("/api/v1/user/email:{email}",email)
                .retrieve()
                .onStatus(
                        status -> status==HttpStatus.NOT_FOUND,
                        clientResponse -> Mono.error(new NotFoundException("User not found"))
                )
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(ApiMessageDTO.class)
                                .switchIfEmpty(Mono.just(ApiMessageDTO.builder().message("Smth get wrong, Status unknown").status(500).build()))
                                .flatMap(errorBody ->
                                        Mono.error(new WebClientExeption(errorBody.getStatus(), "User Service error: " + errorBody.getMessage()))
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
