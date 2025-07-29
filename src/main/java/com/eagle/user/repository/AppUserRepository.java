package com.eagle.user.repository;

import com.eagle.user.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, String> {

    Page<AppUser> findByEnabledTrue(Pageable pageable);

    Optional<AppUser> findTopByOrderByIdDesc();

    Optional<AppUser> findByIdAndEnabledTrue(String id);

    Optional<AppUser> findByEmail(String email);
}
