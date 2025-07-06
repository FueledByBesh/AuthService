package com.lostedin.ecosystem.authservice.service;

import com.lostedin.ecosystem.authservice.dto.user.UserMinDataDTO;
import com.lostedin.ecosystem.authservice.exception.ServiceException;
import com.lostedin.ecosystem.authservice.repository.BrowserUserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrowserService {

    private final BrowserUserRepository browserUserRepository;
    private final UserService userService;


    public UUID getBrowserId(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("BID")) {
                String bid = cookie.getValue();
                try {
                    return UUID.fromString(bid);
                }catch (IllegalArgumentException e){
                    log.error("Couldn't parse bid", e);
                    throw new ServiceException(500, "Smth went wrong with bid");
                }
            }
        }
        return null;
    }

    public List<UserMinDataDTO> getBrowserUsers(UUID browserId) {
        List<UUID> userIds = getUserIdsByBrowserId(browserId);
        return userService.getUsersMinDataByUserId(userIds);
    }

    /**
     * Returns a list of user IDs associated with the given browser ID
     * @param browserId the browser ID to find users for
     * @return a list of user IDs
     */
    public List<UUID> getUserIdsByBrowserId(UUID browserId) {
        return browserUserRepository.getUsersIdByBrowserId(browserId);
    }

    public boolean setBrowserId(HttpServletResponse response, UUID bid, UUID userId) {
        /* TODO: Not Implemented
            method that sets browser id cookie in response and saves it in database
         */
        return false;
    }

    public boolean addUser(UUID bid, UUID user){
        // TODO: Not Implemented
        return false;
    }

    public boolean deleteUser(UUID bid, UUID user){
        //TODO: Not Implemented
        return false;
    }

    public boolean deleteBrowser(UUID bid){
        // TODO: Not Implemented
        return false;
    }



    public List<String> getCookies(HttpServletRequest request){
        List<String> sids = new ArrayList<>();
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            sids.add("There's no cookies");
            return sids;
        }
        for (Cookie cookie : cookies) {
            sids.add(cookie.getName() + ":" + cookie.getValue());
        }
        return sids;
    }






}
