package com.lostedin.ecosystem.authservice.controller;


import com.lostedin.ecosystem.authservice.dto.server.ApiMessageDTO;
import com.lostedin.ecosystem.authservice.dto.session.TokenDTO;
import com.lostedin.ecosystem.authservice.dto.user.UserDTO;
import com.lostedin.ecosystem.authservice.exception.ServiceException;
import com.lostedin.ecosystem.authservice.model.RSAKeyProvider;
import com.lostedin.ecosystem.authservice.service.BasicAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.web.bind.annotation.*;


//TODO: контроллер должен быть @Controller что бы выдавать html страницы
@RestController
@Deprecated
@RequiredArgsConstructor
@RequestMapping("/auth/v1")
public class BasicAuthController {

    private final BasicAuthService basicAuthService;
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

    @GetMapping("/auth")
    protected ResponseEntity<ApiMessageDTO> authorize(
            @RequestParam String service_name,
            @RequestParam String redirect_uri,
            @RequestParam String state){



        return ResponseEntity.ok(ApiMessageDTO.builder().message("authorized").build());
    }

    @PostMapping("/login")
    protected ResponseEntity<TokenDTO> login(@RequestBody UserDTO user) {
        TokenDTO token = basicAuthService.login(user);
        return ResponseEntity.ok(token);
    }

    // удаляет все refresh токены пользователя
    @PostMapping("/logout")
    protected ResponseEntity<ApiMessageDTO> logout(@RequestBody UserDTO userDTO) {
        basicAuthService.logout(userDTO);
        return ResponseEntity.ok(ApiMessageDTO.builder().message("UserImitation logged out successfully").build());
    }

    @PostMapping("/register")
    protected ResponseEntity<TokenDTO> register(@RequestBody UserDTO user) {
        TokenDTO token = basicAuthService.register(user);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh-token")
    protected ResponseEntity<TokenDTO> refresh(@RequestBody TokenDTO token) {
        TokenDTO newToken = basicAuthService.refreshAccessToken(token.getRefreshToken());
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


    //TODO: Rewrite all this shit
}
