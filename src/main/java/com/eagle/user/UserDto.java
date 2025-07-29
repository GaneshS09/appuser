package com.eagle.user;

import java.util.Set;

record UserDto(String id, String email, String password, long mobileNo, String firstName, String middleName, String lastName, String profilePhoto, Set<String> roles, boolean enabled, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired) {

}
