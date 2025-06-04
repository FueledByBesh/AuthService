package com.lostedin.ecosystem.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth/v1")
public class ThymeleafController {


//    @GetMapping("/authorize")
//    public String loginPage(
//            @RequestParam String name,
//            Model model) {
//        model.addAttribute("name",name);
//        return "index.html";
//    }


}
