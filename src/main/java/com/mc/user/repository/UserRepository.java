package com.mc.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mc.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    

}
