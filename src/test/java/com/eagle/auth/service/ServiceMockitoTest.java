package com.eagle.auth.service;

import com.eagle.user.CommonMapper;
import com.eagle.user.dto.UserDto;
import com.eagle.user.entity.AppUser;
import com.eagle.user.entity.AppUserRole;
import com.eagle.user.repository.AppUserRepository;
import com.eagle.user.service.AppUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceMockitoTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private CommonMapper commonMapper;

    @InjectMocks
    private AppUserService appUserService;

    @Test
    void getAllActiveUser_shouldReturnActiveUsers() {
        // Arrange
        Set<AppUserRole> roles = new HashSet<>();
        roles.add(new AppUserRole("ROLE_USER"));

        AppUser activeUser1 = new AppUser(
                "EGL00001", "user1@test.com", "password", 1234567890L,
                "John", "D.", "Doe", "profile.jpg",
                true, true, true, true, roles
        );

        AppUser activeUser2 = new AppUser(
                "EGL00002", "user2@test.com", "password", 9876543210L,
                "Jane", "D.", "Smith", "profile.jpg",
                true, true, true, true, roles
        );

        AppUser inactiveUser = new AppUser(
                "EGL00003", "inactive@test.com", "password", 5555555555L,
                "Inactive", "D.", "User", "profile.jpg",
                false, true, true, true, roles
        );

        // Mock repository response
        when(appUserRepository.findByEnabledTrue()).thenReturn(List.of(activeUser1, activeUser2));

        // Mock mapper conversions
        UserDto dto1 = new UserDto("EGL00001", "user1@test.com", "password",
                1234567890L, "John", "D.", "Doe", "profile.jpg",
                true, true, true, true, Set.of("ROLE_USER"));
        UserDto dto2 = new UserDto("EGL00002", "user2@test.com", "password",
                9876543210L, "Jane", "D.", "Smith", "profile.jpg",
                true, true, true, true, Set.of("ROLE_USER"));

        when(commonMapper.toUserDto(activeUser1)).thenReturn(dto1);
        when(commonMapper.toUserDto(activeUser2)).thenReturn(dto2);

        // Act
        List<UserDto> result = appUserService.getAllActiveUser();

        // Assert
        assertEquals(2, result.size());
        assertEquals("EGL00001", result.get(0).id());
        assertEquals("EGL00002", result.get(1).id());
        assertTrue(result.get(0).roles().contains("ROLE_USER"));
        assertTrue(result.get(1).roles().contains("ROLE_USER"));

        // Verify interactions
        verify(appUserRepository, times(1)).findByEnabledTrue();
        verify(commonMapper, times(1)).toUserDto(activeUser1);
        verify(commonMapper, times(1)).toUserDto(activeUser2);
    }

    @Test
    void getAllActiveUser_withNoActiveUsers_shouldReturnEmptyList() {
        // Arrange
        when(appUserRepository.findByEnabledTrue()).thenReturn(List.of());

        // Act
        List<UserDto> result = appUserService.getAllActiveUser();

        // Assert
        assertTrue(result.isEmpty());
        verify(appUserRepository, times(1)).findByEnabledTrue();
        verifyNoInteractions(commonMapper); // No mapping should happen for empty list
    }
}


//import com.eagle.user.CommonMapper;
//import com.eagle.user.entity.AppUser;
//import com.eagle.user.entity.AppUserRole;
//import com.eagle.user.repository.AppUserRepository;
//import com.eagle.user.repository.AppUserRoleRepository;
//import com.eagle.user.service.AppUserService;
//import com.eagle.user.dto.UserDto;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static org.mockito.Mockito.when;

//@SpringBootTest(classes = {ServiceMockitoTest.class})
//public class ServiceMockitoTest {
//
//    @Mock
//    AppUserRepository appUserRepository;
//
//    @Mock
//    AppUserRoleRepository appUserRoleRepository;
//
//    @InjectMocks
//    CommonMapper commonMapper;
//
//    @InjectMocks
//    AppUserService appUserService;
//
//    public List<UserDto> myUsers;
//
//
//    @Test
//    @Order(1)
//    public void test_getAllActiveUsers() {
//        Set<AppUserRole> roles = new HashSet<>();
//        roles.add(new AppUserRole(1,"ROLE_ADMIN"));
//        roles.add(new AppUserRole(2,"ROLE_MODERATE"));
//
//        List<AppUser> myUsers = new ArrayList<AppUser>();
//        myUsers.add(new AppUser("EGL00001", "ganesh@g.com", "ganesh", 9068682139L, "ganesh", "singh", "gasain", "", true, true, true, true,roles));
//        myUsers.add(new AppUser("EGL00002", "shikha@g.com", "shikha", 9068692139L, "shikha", "singh", "gasain", "", true, true, true, true,roles));

//        Set<String> roles = new HashSet<>();
//        roles.add("ROLE_ADMIN");
//        roles.add("ROLE_MODERATE");
//
//        List<UserDto> myUsers = new ArrayList<UserDto>();
//        myUsers.add(new UserDto("EGL00001", "ganesh@g.com", "ganesh", 9068682139L, "ganesh", "singh", "gasain", "", true, true, true, true,roles));
//        myUsers.add(new UserDto("EGL00002", "shikha@g.com", "shikha", 9068692139L, "shikha", "singh", "gasain", "", true, true, true, true,roles));

//        when(appUserRepository.findByEnabledTrue().stream().map(commonMapper::toUserDto).collect(Collectors.toList())).thenReturn(myUsers.stream().map(commonMapper::toUserDto).collect(Collectors.toList()));
//        appUserService.getAllActiveUser();
//
//    }
//}
