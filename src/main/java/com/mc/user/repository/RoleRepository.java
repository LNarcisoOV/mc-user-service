package com.mc.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mc.user.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
