package com.lostedin.ecosystem.authservice.controller;

import com.lostedin.ecosystem.authservice.dto.User.UserLoginDTO;
import com.lostedin.ecosystem.authservice.dto.User.UserMinDataDTO;
import com.lostedin.ecosystem.authservice.dto.User.UserRegisterDTO;
import com.lostedin.ecosystem.authservice.dto.session.PreSessionDTO;
import com.lostedin.ecosystem.authservice.enums.OAuthResponseType;
import com.lostedin.ecosystem.authservice.exception.ServiceException;
import com.lostedin.ecosystem.authservice.service.AuthService;
import com.lostedin.ecosystem.authservice.service.BrowserService;
import com.lostedin.ecosystem.authservice.service.SessionService;
import com.lostedin.ecosystem.authservice.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/oauth/v1/auth")
public class AuthenticationController {


    /*TODO (идея для будущего апдейта):
     * внедрить state(рандомный идентификатор для сессии)
     * сервис будет отправлять его внутри параметров и использовать этот state во всех
     * последующих запросах внутри сервера
     * State хранить в Redis с expire_time (максимум 30 минут)
     * Если параметр state будет или пустым или expired или нету в Redis запрос будет отклонен
     * (Если добавить state то можно удалить service-name из параметров url)
     * (но все равно state будет передаваться, но хотя бы у него есть роль - безопасность)
     * Status: In Process
     * Вместо state внедрено PreSessions который будет хранятся в Redis (потом, если вспомню кнч)
     */

    private final String url="/oauth/v1/auth";
    private final SessionService sessionService;
    private final BrowserService browserService;
    private final UserService userService;

    @GetMapping("/authorize")
    protected String authenticate(@RequestParam(name = "response_type") String responseType,
                                  @RequestParam(name = "client_id") String clientId,
                                  @RequestParam(name = "state",required = false) String state,
                                  @RequestParam(name = "redirect_uri") String redirectUri,
                                  @RequestParam(name = "scope") String scope,
                                  Model model,
                                  HttpServletRequest request) {

        PreSessionDTO preSession = sessionService
                .generatePreSession(clientId, redirectUri, scope, state, responseType);

        UUID bid = browserService.getBrowserId(request);
        if (bid == null) {
            return "redirect:"+url+"/login?psid="+preSession.getPre_session_id();
        }

        return "redirect:"+url+"/user-list?psid="+preSession.getPre_session_id();
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
                         @RequestBody UserLoginDTO user,Model model){

        Optional<UUID> userId = userService.validateUser(user.getEmail(), user.getPassword());

        if(userId.isEmpty()){
            return "redirect:"+url+"/login?psid=" + preSessionId + "&wrong-credentials=true";
        }

        if(!sessionService.setPreSessionUser(preSessionId, userId.get()))
            throw new ServiceException(500, "Smth went wrong with saving user in presession");


        return "redirect:"+url+"/user-permission?psid=" + preSessionId;
    }

    // Logs out from the current browser session
//    @GetMapping("/logout")
//    protected String logout(Model model, HttpServletRequest request, HttpServletResponse response) {
//        UUID bid = getBrowserId(request);
//        if (bid == null) {
//            return "redirect:/login?service=logout";
//        }
//        response.addCookie(new Cookie("SID", null));
//    }


    @GetMapping("user-list")
    protected String userList(@RequestParam(name = "psid") UUID preSessionId,
                              Model model,HttpServletRequest request) {
        UUID bid = browserService.getBrowserId(request);
        if (bid == null) {
            return "redirect:"+url+"/login?psid=" + preSessionId;
        }

        List<UserMinDataDTO> users = browserService.getBrowserUsers(bid);
        if(users.isEmpty()) {
            model.addAttribute("message", "No users found");
            model.addAttribute("isLoggedIn",false);
        }else {
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("users", users);
        }
        model.addAttribute("psid", preSessionId);
        return "user-list";
    }

    @GetMapping("/user-permission")
    protected String userAccess(@RequestParam(name = "psid") UUID preSessionId,
                                Model model){

        String message = "Will you give access to your account?";
        model.addAttribute("psid", preSessionId);
        model.addAttribute("message", message);
        return "user-permission";
    }

    @PostMapping("/access-given")
    protected String accessGiven(){
        // TODO: return access, refresh token and openID with user minimal data
        //  and redirect to client redirect_uri with state parameter
        return null;
    }

    @PostMapping("/access-denied")
    protected String accessDenied(){
        // TODO: return access denied to client
        //  and redirect to client redirect_uri with state parameter
        return null;
    }


    @GetMapping("/register")
    protected String registerForm(@RequestParam(name = "psid") UUID preSessionId,
                                  Model model) {
        model.addAttribute("psid", preSessionId);
        model.addAttribute("registerUrl", url+"/register?psid="+preSessionId);
        return "register-form";
    }

    @PostMapping("/register")
    protected String register(@RequestParam(name = "psid") UUID preSessionId,
                              @RequestBody UserRegisterDTO user, Model model){

        UUID userId = userService.createUser(user);

        if(userId == null)
            throw new ServiceException(500, "Smth went wrong with creating user");

        if(!sessionService.setPreSessionUser(preSessionId, userId))
            throw new ServiceException(500, "Smth went wrong with saving user in presession");

        return "redirect:"+url+"/user-permission?psid=" + preSessionId;
    }

    @ResponseBody
    @GetMapping("/get-cookies")
    protected List<String> getSID(HttpServletRequest request){
        return browserService.getCookies(request);
    }

    @ResponseBody
    @GetMapping("/health")
    protected String health(){
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


}
