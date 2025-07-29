package com.eagle.user.repository;

import com.eagle.user.entity.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRoleRepository extends JpaRepository<AppUserRole, Integer> {

    AppUserRole findByRoleName(String role);
}
