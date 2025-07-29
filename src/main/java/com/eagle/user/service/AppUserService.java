package com.eagle.user.service;

import com.eagle.user.CommonMapper;
import com.eagle.user.dto.UserDto;
import com.eagle.user.entity.AppUser;
import com.eagle.user.entity.AuthToken;
import com.eagle.user.repository.AppUserRepository;
import com.eagle.user.repository.AuthTokenRepository;
import com.eagle.util.EncryptDecrypt;
import com.eagle.util.IdGenerator;
import com.eagle.util.Patcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final AuthTokenRepository authTokenRepository;
    private final CommonMapper commonMapper;
    private final EncryptDecrypt encryptDecrypt;
    private final Patcher patcher;



    public AppUserService(AppUserRepository appUserRepository, AuthTokenRepository authTokenRepository, CommonMapper commonMapper, EncryptDecrypt encryptDecrypt, Patcher patcher) {
        this.appUserRepository = appUserRepository;
        this.authTokenRepository = authTokenRepository;
        this.commonMapper = commonMapper;
        this.encryptDecrypt = encryptDecrypt;
        this.patcher = patcher;
    }

     public List<UserDto> getAllActiveUser(){
        return appUserRepository.findByEnabledTrue().stream().map(commonMapper::toUserDto).collect(Collectors.toList());
    }

    public Optional<AppUser> getUserById(String id){
        return appUserRepository.findByIdAndEnabledTrue(id);
    }

     public ResponseEntity<UserDto> register(UserDto userDto) throws IOException {
        try{
            AppUser appUser = commonMapper.toUser(userDto);

            appUser.setId(generateUserId("EGL"));
            appUser.setPassword(encryptDecrypt.encrypt(userDto.password()));
//            System.out.println("ganesh appUser "+ appUser.getRoles() + " ===== " + userDto.roles());
           appUserRepository.save(appUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(commonMapper.toUserDto(appUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(userDto);
        }

    }

      public ResponseEntity<UserDto> update(String id, UserDto userDto) throws IllegalAccessException {
        AppUser optionalUser = appUserRepository.findById(id).get();
            if(optionalUser == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userDto);
            } else {
                AppUser user = commonMapper.toUser(userDto);
                Patcher.fieldPatcher(optionalUser, user);

                appUserRepository.save(optionalUser);
            }
            return ResponseEntity.status(HttpStatus.OK).body(commonMapper.toUserDto(optionalUser));
    }

    public String generateUserId(String prefix) {
        IdGenerator<AppUser > userIdGenerator = new IdGenerator<>(
                AppUser::getId,
                fullId -> Long.parseLong(fullId.substring(3)),
                num -> String.format("%05d", num)
        );

        Optional<AppUser > lastUser  = appUserRepository.findTopByOrderByIdDesc();
        return userIdGenerator.generateId(prefix, lastUser );
    }

     public Optional<AppUser> softDeleteUser(String id){
        return appUserRepository.findByIdAndEnabledTrue(id).map(user -> {
            user.setEnabled(false);
            user.setAccountNonLocked(false);
            return Optional.of(appUserRepository.save(user));
        }).orElse(Optional.empty());
    }

    public Optional<AppUser> getByEmailId(String email){
        return appUserRepository.findByEmail(email);
    }


    public void saveAuthToken(String authTokenValue, boolean tokenRevoked, boolean tokenExpired, AppUser appUser){
        AuthToken authToken = new AuthToken();
       authToken.setTokenValue(authTokenValue);
       authToken.setTokenRevoked(tokenRevoked);
       authToken.setTokenExpired(tokenExpired);
       authToken.setAppUser(appUser);

        authTokenRepository.save(authToken);
    }
}
