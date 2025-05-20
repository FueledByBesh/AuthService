package space.lostedin.accounts.authservice.service;

import space.lostedin.accounts.authservice.dto.TokenDTO;
import space.lostedin.accounts.authservice.dto.UserDTO;
import space.lostedin.accounts.authservice.entity.RefreshToken;
import space.lostedin.accounts.authservice.exception.ServiceException;
import space.lostedin.accounts.authservice.model.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JWTUtil jwtUtil;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final Long USER_ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 15L; // 15 минут
    private final Long USER_REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 4L; // 4 дня


    public TokenDTO register(UserDTO userDTO) {
        isCorrectDTO(userDTO);
        UserDTO createdUser = userService.createUser(userDTO);
        return generateUserToken(createdUser);
    }

    public TokenDTO login(UserDTO userDTO) {

        isCorrectDTO(userDTO);

        // Проверяем, существует ли пользователь
        Optional<UserDTO> optionalExistingUser;
        if(userDTO.getUsername() != null) {
            optionalExistingUser = userService.getUserByUsername(userDTO.getUsername());
        } else if(userDTO.getEmail() != null) {
            optionalExistingUser = userService.getUserByEmail(userDTO.getEmail());
        } else {
            throw new ServiceException(400,"Username or email must be provided");
        }

        if (optionalExistingUser.isEmpty()) {
            throw new ServiceException(401, "Invalid username or email");
        }

        UserDTO existingUser = optionalExistingUser.get();

        // Проверяем пароль

        //TODO: Password validation должен быть в UserService (микросервис), а не тут.
        // Этот AuthService не должен иметь доступ к паролям

        if (!passwordEncoder.matches(userDTO.getPassword(), existingUser.getPassword())) {
            throw new ServiceException(401, "Invalid password");
        }

        // Генерируем токены
        return generateUserToken(existingUser);
    }


    //TODO: Логику logout нужно обновить в будущем!!!
    // Потому что любой пользователь имеющий non-expired токен может выйти из системы другого пользователя зная его id, username или email.
    // Лучше написать с нуля оптимизируя или создать новый метод который будет удалять токены вытаскивая userId из токена
    public void logout(UserDTO userDTO) {

        if(userDTO.getId() == null && userDTO.getUsername() == null && userDTO.getEmail() == null) {
            throw new ServiceException(400,"At least one UserImitation info must be provided (User_ID, Username or Email)");
        }

        UUID userId;
        if(userDTO.getUsername() != null) {
            Optional<UserDTO> optionalExistingUser = userService.getUserByUsername(userDTO.getUsername());
            if (optionalExistingUser.isPresent()) {
                userId = optionalExistingUser.get().getId();
            } else {
                throw new ServiceException(401, "Invalid username");
            }
        } else if(userDTO.getEmail() != null) {
            Optional<UserDTO> optionalExistingUser = userService.getUserByEmail(userDTO.getEmail());
            if (optionalExistingUser.isPresent()) {
                userId = optionalExistingUser.get().getId();
            } else {
                throw new ServiceException(401, "Invalid email");
            }
        } else {
            userId = userDTO.getId();
        }

        refreshTokenService.deleteRefreshTokenByUserId(userId);
    }

    private TokenDTO generateUserToken(UserDTO userDTO) {

        String accessToken = jwtUtil.generateUserToken(userDTO.getId().toString(),userDTO.getUsername(), USER_ACCESS_TOKEN_EXPIRATION_TIME);
        String refreshToken = jwtUtil.generateUserToken(userDTO.getId().toString(),userDTO.getUsername(), USER_REFRESH_TOKEN_EXPIRATION_TIME);

        refreshTokenService.createRefreshToken(userDTO.getId(), refreshToken ,USER_REFRESH_TOKEN_EXPIRATION_TIME);

        return TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void isCorrectDTO(UserDTO userDTO) {
        if(userDTO.getUsername()==null && userDTO.getEmail()==null) {
            throw new ServiceException(400,"Username or email must be provided");
        }
        if(userDTO.getPassword() == null) {
            throw new ServiceException(400,"Password must be provided");
        }
    }

    public TokenDTO refreshAccessToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new ServiceException(400, "Refresh token is required");
        }

        // Вытаскиваем refresh token из базы данных
        Optional<RefreshToken> refreshTokenOptional = refreshTokenService.findByToken(refreshToken);


        // Проверяем, существует ли refresh token
        if (refreshTokenOptional.isEmpty()) {
            throw new ServiceException(401, "Invalid refresh token or has been expired");
        }

        // Проверяем, не истек ли refresh token
        if (refreshTokenService.isTokenExpired(refreshTokenOptional.get())) {
            throw new ServiceException(401, "Refresh token has expired");
        }

        // Генерируем новый access token

        Map<String, String> subAndClaims = jwtUtil.getSubAndClaims(refreshToken);

        String sub = subAndClaims.get("sub");
        String username = subAndClaims.get("username");
        String accessToken = jwtUtil.generateUserToken(sub,username, USER_ACCESS_TOKEN_EXPIRATION_TIME);

        return TokenDTO.builder()
                .accessToken(accessToken)
                .build();
    }

}
