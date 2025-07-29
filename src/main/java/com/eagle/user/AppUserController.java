package com.eagle.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/eagle/user")
class AppUserController {

    private final AppUserService appUserService;

    AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    ResponseEntity<List<AppUser>> getAllActiveUser(){
     return ResponseEntity.ok(appUserService.getAllActiveUser());
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> getUserById(@PathVariable String id){
        return appUserService.getUserById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    private ResponseEntity<UserDto> register(@RequestBody UserDto userDto) throws IOException {
        return appUserService.register(userDto);
    }

    @PatchMapping("/{id}")
    private ResponseEntity<UserDto> update(@PathVariable String id, @RequestBody UserDto userDto) throws IOException, IllegalAccessException {
        return appUserService.update(id,userDto);
    }

    @DeleteMapping("/{id}")
     private ResponseEntity<?> deleteUser(@PathVariable String id){
        return appUserService.softDeleteUser(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

}
