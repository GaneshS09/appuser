package com.eagle.auth.service;


import com.eagle.auth.dto.request.LoginRequestDto;
import com.eagle.auth.dto.response.LoginResponse;
import com.eagle.user.entity.AppUser;
import com.eagle.user.service.AppUserService;
import com.eagle.user.repository.AuthTokenRepository;
import com.eagle.util.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthUserService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AuthTokenRepository authTokenRepository;
    private final AppUserService appUserService;

    public AuthUserService(AuthenticationManager authenticationManager, JwtUtils jwtUtils, AuthTokenRepository authTokenRepository, AppUserService appUserService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.authTokenRepository = authTokenRepository;
        this.appUserService = appUserService;
    }

    public ResponseEntity<?> authenticateUser(LoginRequestDto loginRequestDto){

        Optional<AppUser> appUser = appUserService.getByEmailId(loginRequestDto.email());

        if(appUser.isEmpty()) return ResponseEntity.notFound().build();

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.email(),loginRequestDto.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUserDetails appUserDetails = (AppUserDetails)  authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(appUserDetails);

//        List<String> roles = appUserDetails.getAuthorities()
//                .stream()
//                .map(item -> item.getAuthority())
//                .collect(Collectors.toList());
        List<String> roles = appUserDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        appUserService.saveAuthToken(jwtCookie.getValue(),false,false,appUser.get());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new LoginResponse(appUserDetails.getId(),roles));
    }

}
