package com.lostedin.ecosystem.authservice.controller;

import com.lostedin.ecosystem.authservice.dto.oauthflow.AuthorizeDTO;
import com.lostedin.ecosystem.authservice.dto.user.UserLoginDTO;
import com.lostedin.ecosystem.authservice.dto.user.UserMinDataDTO;
import com.lostedin.ecosystem.authservice.dto.user.UserRegisterDTO;
import com.lostedin.ecosystem.authservice.dto.session.PreSessionCreateDTO;
import com.lostedin.ecosystem.authservice.enums.OAuthFlowParameterTypes.CodeChallengeMethodType;
import com.lostedin.ecosystem.authservice.enums.OAuthFlowParameterTypes.OAuthResponseType;
import com.lostedin.ecosystem.authservice.exception.ServiceException;
import com.lostedin.ecosystem.authservice.model.Helper;
import com.lostedin.ecosystem.authservice.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/oauth2/v1/auth")
public class AuthController {


    /* TODO: Should Implement (Status: Not Implemented Yet)
        1) Вернуть клиенту scope-granted (scopes которые приняты)
        2) PKCE для публичных клиентов (Optional for confidential clients)
        3) Implement Refresh token rotation
     */

    private final String url = "/oauth2/v1/auth";
    private final OAuthFlowService oAuthFlowService;
    private final SessionService sessionService;
    private final BrowserService browserService;
    private final UserService userService;

    @GetMapping("/authorize")
    protected String authorize(@RequestParam(name = "response_type") String responseType,
                               @RequestParam(name = "client_id") String clientId,
                               @RequestParam(name = "state", required = false) String state,
                               @RequestParam(name = "redirect_uri") String redirectURI,
                               @RequestParam(name = "scope") String scope,
                               @RequestParam(name = "code_challenge", required = false) String codeChallenge,
                               @RequestParam(name = "code_challenge_method", required = false) String codeChallengeMethod,
                               HttpServletRequest request) {


        AuthorizeDTO authDto = createAuthorizeDTO(
                clientId,
                responseType,
                redirectURI,
                scope, state,
                codeChallenge,
                codeChallengeMethod
        );

        UUID preSessionId = oAuthFlowService.startAuthorizationFlow(authDto);

        UUID bid = browserService.getBrowserId(request);
        if (bid == null) {
            return "redirect:" + UriComponentsBuilder.fromUriString(url + "/login")
                    .queryParam("psid", preSessionId)
                    .build();
        }


        return "redirect:" + UriComponentsBuilder.fromUriString(url + "/user-list")
                .queryParam("psid", preSessionId)
                .build();
    }

    @GetMapping("/login")
    protected String login(@RequestParam(name = "psid") UUID preSessionId,
                           @RequestParam(name = "wrong-credentials", required = false, defaultValue = "false")
                           boolean wrongCredentials,
                           Model model, HttpServletRequest request) {
        model.addAttribute("psid", preSessionId);
        model.addAttribute("wrongCredentials", wrongCredentials);
        return "login-form";
    }

    @PostMapping("/log-in")
    protected String login(@RequestParam(name = "psid") UUID preSessionId,
                           @RequestBody UserLoginDTO user, Model model) {

        Optional<UUID> userId = userService.validateUser(user.getEmail(), user.getPassword());

        if (userId.isEmpty()) {
            return "redirect:" + UriComponentsBuilder.fromUriString(url + "/login")
                    .queryParam("psid", preSessionId)
                    .queryParam("wrong-credentials", true)
                    .build();
        }

        sessionService.setPreSessionUser(preSessionId, userId.get());


        return "redirect:" + UriComponentsBuilder.fromUriString(url + "/user-permission")
                .queryParam("psid", preSessionId)
                .build();
    }

    @GetMapping("user-list")
    protected String userList(@RequestParam(name = "psid") UUID preSessionId,
                              Model model, HttpServletRequest request) {
        UUID bid = browserService.getBrowserId(request);
        if (bid == null) {
            return "redirect:" + UriComponentsBuilder.fromUriString(url + "/login")
                    .queryParam("psid", preSessionId)
                    .build();
        }

        List<UserMinDataDTO> users = browserService.getBrowserUsers(bid);
        if (users.isEmpty()) {
            model.addAttribute("message", "No users found");
            model.addAttribute("isLoggedIn", false);
        } else {
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("users", users);
        }
        model.addAttribute("psid", preSessionId);
        return "user-list";
    }

    @GetMapping("/user-permission")
    protected String userAccess(@RequestParam(name = "psid") UUID preSessionId,
                                Model model) {

        String message = "Will you give access to your account?";
        model.addAttribute("psid", preSessionId);
        model.addAttribute("message", message);
        return "user-permission";
    }

    @GetMapping("/access-given")
    protected String accessGiven(@RequestParam(name = "psid") UUID preSessionId) {
        String clientCallbackUri = oAuthFlowService.handleAuthorizationCallback(preSessionId);
        return "redirect:" + clientCallbackUri;
    }

    @GetMapping("/access-denied")
    protected String accessDenied(@RequestParam(name = "psid") UUID preSessionId) {
        String clientCallbackUri = oAuthFlowService.handleAuthorizationCallbackAccessDenied(preSessionId);
        return "redirect:" + clientCallbackUri;
    }


    @GetMapping("/register")
    protected String registerForm(@RequestParam(name = "psid") UUID preSessionId,
                                  Model model) {
        model.addAttribute("psid", preSessionId);
        model.addAttribute("registerUrl",
                UriComponentsBuilder.fromUriString(url + "/register")
                        .queryParam("psid", preSessionId).build().toUriString());
        return "register-form";
    }


    @PostMapping("/register")
    protected String register(@RequestParam(name = "psid") UUID preSessionId,
                              @RequestBody UserRegisterDTO user) {

        /* TODO: 07.07.2025 Status: Not Implemented
            1) Check if the email has been used before or not
            2) Confirm Email by sending code or smth else
            2) Save user into browser cookies
         */

        UUID userId = userService.createUser(user);

        if (userId == null) {
            log.error("Couldn't create user");
            throw new ServiceException(500, "Smth went wrong with creating user");
        }

        sessionService.setPreSessionUser(preSessionId, userId);

        // TODO: 07.07.2025 Should Save user into browser cookies

        return "redirect:" + UriComponentsBuilder.fromUriString(url + "/user-permission")
                .queryParam("psid", preSessionId)
                .build();
    }

    // write confirm email

    @ResponseBody
    @GetMapping("/get-cookies")
    protected List<String> getSID(HttpServletRequest request) {
        return browserService.getCookies(request);
    }

    @ResponseBody
    @GetMapping("/health")
    protected String health() {
        // TODO: Not Implemented
        return "Health";
    }

    private void setCookie(HttpServletResponse response) {

        UUID uuid = UUID.randomUUID();
        Cookie cookie = new Cookie("BID", uuid.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setDomain("lostedin.com");
        System.out.println(uuid);
        response.addCookie(cookie);

    }

    private AuthorizeDTO createAuthorizeDTO(String clientId,
                                            String responseType, String redirectURI,
                                            String scope, String state,
                                            String codeChallenge, String codeChallengeMethod) {

        OAuthResponseType oAuthResponseType;
        CodeChallengeMethodType codeChallengeMethodType;

        try {
            oAuthResponseType = Helper.getOauthResponseType(responseType);
        } catch (IllegalArgumentException e) {
            throw new ServiceException(400, "Bad Request: Unknown response type");
        }
        try {
            if (codeChallenge != null)
                codeChallengeMethodType = Helper.getCodeChallengeMethodType(codeChallengeMethod);
            else
                codeChallengeMethodType = null;
        } catch (IllegalArgumentException e) {
            throw new ServiceException(400, "Bad Request: Unknown code challenge method");
        }

        return AuthorizeDTO.builder()
                .clientId(clientId)
                .responseType(oAuthResponseType)
                .redirectURI(redirectURI)
                .scope(scope)
                .state(state)
                .codeChallenge(codeChallenge)
                .codeChallengeMethod(codeChallengeMethodType)
                .build();
    }


}
