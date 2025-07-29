package com.eagle.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class CustomLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                // Clear specific cookies or all cookies
                if (cookie.getName().equals("eagle") || cookie.getName().startsWith("eagle")) {
                    Cookie cookieToDelete = new Cookie(cookie.getName(), null);
                    cookieToDelete.setMaxAge(0);
                    cookieToDelete.setPath("/eagle"); // Ensure the path matches the original cookie
                    response.addCookie(cookieToDelete);
                }
            }
        }
    }
}
