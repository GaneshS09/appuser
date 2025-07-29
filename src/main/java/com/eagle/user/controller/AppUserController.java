package com.eagle.user.controller;

import com.eagle.user.service.AppUserService;
import com.eagle.user.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/eagle/user")
class AppUserController {

    private final AppUserService appUserService;

    AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    ResponseEntity<Page<UserDto>> getAllActiveUser(@PathVariable(name = "page", value = "0") int page,
                                                   @PathVariable(name = "size", value = "1000") int size){
     return ResponseEntity.ok(appUserService.getAllActiveUser(page,size));
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getUserById(@PathVariable String id){
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
