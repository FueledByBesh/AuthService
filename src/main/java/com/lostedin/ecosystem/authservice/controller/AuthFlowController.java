package com.lostedin.ecosystem.authservice.controller;

import com.lostedin.ecosystem.authservice.dto.user.UserLoginDTO;
import com.lostedin.ecosystem.authservice.dto.user.UserMinDataDTO;
import com.lostedin.ecosystem.authservice.service.BrowserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth/auth-flow")
public class AuthFlowController {

    private final String authFlowUrl = "/auth/auth-flow";
    private final String oAut2hurl = "/auth/oauth2";

    private final BrowserService browserService;


    @GetMapping("/v1/login")
    protected String login(@RequestParam(name = "psid") UUID preSessionId,
                           @RequestParam(name = "wrong-credentials", required = false, defaultValue = "false")
                           boolean wrongCredentials,
                           Model model, HttpServletRequest request) {
        model.addAttribute("psid", preSessionId);
        model.addAttribute("wrongCredentials", wrongCredentials);
        model.addAttribute("user", UserLoginDTO.builder().build());
        return "login-form";
    }

    @GetMapping("/v1/user-list")
    protected String userList(@RequestParam(name = "psid") UUID preSessionId,
                              Model model, HttpServletRequest request) {
        UUID bid = browserService.getBrowserId(request);
        if (bid == null) {
            return "redirect:" + UriComponentsBuilder.fromUriString(authFlowUrl + "/v1/login")
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

    @GetMapping("/v1/user-permission")
    protected String userAccess(@RequestParam(name = "psid") UUID preSessionId,
                                Model model) {

        String message = "Will you give access to your account?";
        model.addAttribute("psid", preSessionId);
        model.addAttribute("message", message);
        return "user-permission";
    }


    // test method
    @PostMapping("/login-test")
    protected String logintest(@RequestParam(name = "psid") UUID preSessionId,
                               @ModelAttribute UserLoginDTO user, Model model) {

        System.out.println(user.getEmail()+" : "+user.getEmail());
        return "redirect:" + UriComponentsBuilder.fromUriString(authFlowUrl + "/home")
                .queryParam("psid", preSessionId)
                .queryParam("user", user.getEmail())
                .build();
    }
    // test method
    @GetMapping("/home")
    protected String home(@RequestParam(name = "psid") UUID preSessionId,
                          @RequestParam(name = "user") String user,
                          Model model){
        model.addAttribute("psid", preSessionId);
        model.addAttribute("user",user);
        return "home";
    }
}
