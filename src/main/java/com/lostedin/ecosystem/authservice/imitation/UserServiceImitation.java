package com.lostedin.ecosystem.authservice.imitation;

import com.lostedin.ecosystem.authservice.dto.User.UserDTO;
import com.lostedin.ecosystem.authservice.exception.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserServiceImitation {

    private final PasswordEncoder passwordEncoder;

    Map<UUID, UserImitation> userDB = new HashMap<>();

    public UserDTO createUser(UserDTO userDTO) {

        UserImitation userImitation = UserImitation.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(encodePassword(userDTO.getPassword()))
                .build();
        if(userDB.containsKey(userImitation.getId())){
            //recreating UserImitation, because generated UUID already exists
            return createUser(userDTO);
        }

        userDB.put(userImitation.getId(), userImitation);

        return UserDTO.builder()
                .id(userImitation.getId())
                .username(userImitation.getUsername())
                .email(userImitation.getEmail())
                .build();
    }

    public Optional<UserDTO> getUserById(UUID id) {

        if(userDB.containsKey(id)){
            UserImitation userImitation = userDB.get(id);
            return Optional.of(UserDTO.builder()
                    .id(userImitation.getId())
                    .username(userImitation.getUsername())
                    .email(userImitation.getEmail())
                    .password(userImitation.getPassword())
                    .build());
        }

        return Optional.empty();
    }

    public Optional<UserDTO> getUserByUsername(String username) {

        //TODO: неправильная логика, userImitation.getUsername() might be null
        // либо нужно изменить логику тут, либо при создании пользователя ставить дефолтный username
        // или вообще отказать созданию пользователя без username или же email
        // PS: пока логика исправлена, но нужно писать другую логику. Хотя зачем? Все равно же создам
        // отдельный микросервис
        for (UserImitation userImitation : userDB.values()) {
            if (userImitation.getUsername()!=null && userImitation.getUsername().equals(username)) {
                return Optional.of(UserDTO.builder()
                        .id(userImitation.getId())
                        .username(userImitation.getUsername())
                        .email(userImitation.getEmail())
                        .password(userImitation.getPassword())
                        .build());
            }
        }

        return Optional.empty();
    }

    public Optional<UserDTO> getUserByEmail(String email) {

        // TODO: та же проблема как в getUserByUsername
        for (UserImitation userImitation : userDB.values()) {
            if (userImitation.getEmail()!=null && userImitation.getEmail().equals(email)) {
                return Optional.of(UserDTO.builder()
                        .id(userImitation.getId())
                        .username(userImitation.getUsername())
                        .email(userImitation.getEmail())
                        .password(userImitation.getPassword())
                        .build());
            }
        }

        return Optional.empty();
    }


    public void saveDB(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user.ser"))) {
            oos.writeObject(userDB);
            System.out.println("Объект сериализован в user.ser");
        } catch (IOException e) {
            throw new RuntimeException("Error saving userDB to file", e);
        }

    }

    public List<String> getAllUsers(){
        List<String> users = new ArrayList<>();
        for (UserImitation userImitation : userDB.values()) {
            users.add(userImitation.getUsername()+" "+ userImitation.getEmail());
        }

        if(users.isEmpty()){
            System.out.println("No users found");
            throw new Message("No Users found");
        }
        return users;
    }

    public void loadDB(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user.ser"))) {
            userDB = (Map<UUID, UserImitation>) ois.readObject();
            System.out.println("Объект десериализован из user.ser");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error loading userDB from file", e);
        }

    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

}
