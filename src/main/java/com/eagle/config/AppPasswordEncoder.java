package com.eagle.config;

import com.eagle.util.EncryptDecrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AppPasswordEncoder implements PasswordEncoder {

    @Autowired
    EncryptDecrypt encryptDecrypt;

    @Override
    public String encode(CharSequence rawPassword) {
        return AppEncryptedMethod(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String decryptedPassword = AppDecryptedMethod(encodedPassword);
        return decryptedPassword.equals(rawPassword.toString());
    }

    private String AppEncryptedMethod(String password){
        return encryptDecrypt.encrypt(password);
    }

    private String AppDecryptedMethod(String encryptedPassword){
        return encryptDecrypt.decrypt(encryptedPassword);
    }
}
