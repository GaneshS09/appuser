package com.eagle.util;

import com.eagle.auth.service.AppUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${security.jwt.secret-key}")
    private String jwtSecret;

    @Value("${security.jwt.expiration-time}")
    private int jwtExpirationMs;

    @Value("${security.jwt.cookie-name}")
    private String jwtCookie;

    private final EncryptDecrypt encryptDecrypt;

    public JwtUtils(EncryptDecrypt encryptDecrypt) {
        this.encryptDecrypt = encryptDecrypt;
    }

    public String getJwtFromCookies(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if(cookie !=null){
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(AppUserDetails appUserDetails){
        String jwt = generateTokenFromUserName(appUserDetails.getUsername());
        String encryptJwt = encryptDecrypt.encrypt(jwt);
        ResponseCookie cookie = ResponseCookie.from(jwtCookie,encryptJwt).path("/eagle").maxAge(24 * 60 *60).httpOnly(true).build();
        return cookie;
    }

    public ResponseCookie getCleanJwtCookie(){
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/eagle").build();
        return cookie;
    }

    public String getUserNameFromJwtToken(String token){
        return  Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateTJwtoken(String token){
        try{
            Jws tst = Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token);
            if(!tst.getPayload().equals(null)){
                return true;
            }
        } catch (MalformedJwtException e) {
//            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
//            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
//            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
//            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String generateTokenFromUserName(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key())
                .compact();

    }
}
