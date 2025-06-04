package com.lostedin.ecosystem.authservice.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth/v1")
public class AuthenticationController {

    @GetMapping("/authenticate")
    public String authenticate(@RequestParam String service,
                               Model model,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies==null || cookies.length==0) {
            setCookie(response);
        }else {
            System.out.println(cookies[0].getValue());
        }

        model.addAttribute("service", service);
        return "index.html";
    }

    private void setCookie(HttpServletResponse response) {

        UUID uuid = UUID.randomUUID();
        Cookie cookie = new Cookie("SID", uuid.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        System.out.println(uuid);
        response.addCookie(cookie);

    }


}
