package com.eagle.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, String> {

    List<AppUser> findByEnabledTrue();

    Optional<AppUser> findTopByOrderByIdDesc();

    Optional<AppUser> findByIdAndEnabledTrue(String id);

    Optional<AppUser> findByEmail(String email);
}
