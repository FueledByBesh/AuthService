package space.lostedin.accounts.authservice.imitation;


import space.lostedin.accounts.authservice.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserControllerImitation {

    private final UserServiceImitation userService;

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO user = userService.createUser(userDTO);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/id:{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {

        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username:{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {

        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @PutMapping("/save")
    public ResponseEntity<String> saveDB(){
        userService.saveDB();
        return ResponseEntity.ok("DB saved successfully");
    }

    @PutMapping("/load")
    public ResponseEntity<String> loadDB(){
        userService.loadDB();
        return ResponseEntity.ok("DB loaded successfully");
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<String>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

}
