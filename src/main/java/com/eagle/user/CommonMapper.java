package com.eagle.user;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
class CommonMapper {

    private final AppUserRoleRepository appUserRoleRepository;

    CommonMapper(AppUserRoleRepository appUserRoleRepository) {
        this.appUserRoleRepository = appUserRoleRepository;
    }

    AppUser toUser(UserDto userDto) {

        Set<AppUserRole> appRoles =  new HashSet<>();
        for(String role : userDto.roles()){
//            System.out.println("Ganesh role "+role+" ===== "+appUserRoleRepository.findByRoleName(role));
          AppUserRole appuserroles = appUserRoleRepository.findByRoleName(role);
            System.out.println("Ganesh re "+ appuserroles+" ===== ");

            appRoles.add(appuserroles);
        }

        System.out.println("ganesh appRoles "+appRoles);
        return new AppUser(
                userDto.id(),
                userDto.email(),
                userDto.password(),
                userDto.mobileNo(),
                userDto.firstName(),
                userDto.middleName(),
                userDto.lastName(),
                userDto.profilePhoto(),
                userDto.enabled(),
                userDto.accountNonLocked(),
                userDto.accountNonExpired(),
                userDto.credentialsNonExpired(),
                appRoles
        );
    }


    UserDto toUserDto(AppUser appUser) {

        Set<String> roles = new HashSet<>();
        for(AppUserRole role : appUser.getRoles()){
            roles.add(role.getRoleName());
        }


        return new UserDto(
                appUser.getId(),
                appUser.getEmail(),
                appUser.getPassword(),
                appUser.getMobileNo(),
                appUser.getFirstName(),
                appUser.getMiddleName(),
                appUser.getLastName(),
                appUser.getProfilePhoto(),
                roles,
                appUser.isEnabled(),
                appUser.isAccountNonLocked(),
                appUser.isAccountNonExpired(),
                appUser.isCredentialsNonExpired()
        );
    }
}