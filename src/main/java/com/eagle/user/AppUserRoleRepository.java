package com.eagle.user;

import org.springframework.data.jpa.repository.JpaRepository;

interface AppUserRoleRepository extends JpaRepository<AppUserRole, Integer> {

    AppUserRole findByRoleName(String role);
}
