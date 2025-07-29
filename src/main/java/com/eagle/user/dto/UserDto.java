package com.eagle.user.dto;

import java.util.Set;

public record UserDto(String id, String email, String password, long mobileNo, String firstName, String middleName, String lastName, String profilePhoto, boolean enabled, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired,Set<String> roles) {

}
