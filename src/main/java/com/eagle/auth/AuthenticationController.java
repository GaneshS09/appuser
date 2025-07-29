package com.eagle.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/eagle/auth")
class AuthenticationController {

    private final AuthUserService authUserService;

    AuthenticationController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @PostMapping
    ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDto loginRequestDto) {
        return authUserService.authenticateUser(loginRequestDto);
    }

}