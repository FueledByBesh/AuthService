package space.lostedin.accounts.authservice.service;

import space.lostedin.accounts.authservice.dto.ApiMessageDTO;
import space.lostedin.accounts.authservice.dto.UserDTO;
import space.lostedin.accounts.authservice.exception.Message;
import space.lostedin.accounts.authservice.exception.NotFoundException;
import space.lostedin.accounts.authservice.exception.WebClientExeption;
import space.lostedin.accounts.authservice.imitation.UserImitation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;


//This class will imitate the UserService till it is implemented

@Service
@RequiredArgsConstructor
public class UserService {

    @Qualifier("userServiceWebClient")
    private final WebClient userServiceWebClient;
//    Map<UUID, UserImitation> userDB = new HashMap<>();

    public UserDTO createUser(UserDTO userDTO) {

        return userServiceWebClient.get()
                .uri("/api/v1/user/create")
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
        //TODO: Implement the logic to get user by email

//        for (UserImitation userImitation : userDB.values()) {
//            if (userImitation.getEmail() != null && userImitation.getEmail().equals(email)) {
//                return Optional.of(UserDTO.builder()
//                        .id(userImitation.getId())
//                        .username(userImitation.getUsername())
//                        .email(userImitation.getEmail())
//                        .password(userImitation.getPassword())
//                        .build());
//            }
//        }

        return Optional.empty();
    }

    public List<String> getAllUsers() {
//        List<String> users = new ArrayList<>();
//        for (UserImitation userImitation : userDB.values()) {
//            users.add(userImitation.getUsername() + " " + userImitation.getEmail());
//        }
//
//        if (users.isEmpty()) {
//            System.out.println("No users found");
//            throw new Message("No Users found");
//        }
        return new ArrayList<>();
    }
}
