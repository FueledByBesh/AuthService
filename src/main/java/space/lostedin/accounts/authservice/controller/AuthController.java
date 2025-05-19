package space.lostedin.accounts.authservice.controller;


import space.lostedin.accounts.authservice.dto.ApiMessageDTO;
import space.lostedin.accounts.authservice.dto.TokenDTO;
import space.lostedin.accounts.authservice.dto.UserDTO;
import space.lostedin.accounts.authservice.exception.ServiceException;
import space.lostedin.accounts.authservice.model.RSAKeyProvider;
import space.lostedin.accounts.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final FilterChainProxy filterChainProxy;
    private final RSAKeyProvider rsaKeyProvider;

    @GetMapping("/filterchains")
    protected String printFilterchians(){
        filterChainProxy.getFilterChains().forEach(chain -> {
            System.out.println("Filters in security chain:");
            chain.getFilters().forEach(filter -> System.out.println(" - " + filter.getClass().getSimpleName()));
        });
        return "Filter chains printed to console";
    }

    @PostMapping("/login")
    protected ResponseEntity<TokenDTO> login(@RequestBody UserDTO user) {
        TokenDTO token = authService.login(user);
        return ResponseEntity.ok(token);
    }

    //удаляет все refresh токены пользователя
    @PostMapping("/logout")
    protected ResponseEntity<ApiMessageDTO> logout(@RequestBody UserDTO userDTO) {
        authService.logout(userDTO);
        return ResponseEntity.ok(ApiMessageDTO.builder().message("UserImitation logged out successfully").build());
    }

    @PostMapping("/register")
    protected ResponseEntity<TokenDTO> register(@RequestBody UserDTO user) {
        TokenDTO token = authService.register(user);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh-token")
    protected ResponseEntity<TokenDTO> refresh(@RequestBody TokenDTO token) {
        TokenDTO newToken = authService.refreshAccessToken(token.getRefreshToken());
        return ResponseEntity.ok(newToken);
    }

    @PostMapping("/service-token")
    protected ResponseEntity<TokenDTO> serviceToken(String serviceName) {
        //TODO: реализовать получение токена для сервиса
        throw new ServiceException(501, "Feature not implemented");
    }

    @GetMapping("/public-key")
    protected ResponseEntity<String> publicKey() {
        return ResponseEntity.ok(rsaKeyProvider.getPublicKey());
    }

    //TODO: написать UserService иммитатор +++

    //TODO: подключить к БД   +++

    //TODO: дописать этот контроллер

    //TODO: вывести токены на отдельный контроллер TokenController (надо подумать правильно ли это)
    //PS: думаю это тупо, потому что токены это часть аутентификации
}
