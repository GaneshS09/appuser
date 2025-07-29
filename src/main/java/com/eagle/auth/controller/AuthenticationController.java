package com.eagle.auth.controller;


import com.eagle.auth.dto.request.LoginRequestDto;
import com.eagle.auth.service.AuthUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/eagle/auth")
public class AuthenticationController {

    private final AuthUserService authUserService;

    AuthenticationController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @PostMapping
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDto loginRequestDto) {
        return authUserService.authenticateUser(loginRequestDto);
    }

}