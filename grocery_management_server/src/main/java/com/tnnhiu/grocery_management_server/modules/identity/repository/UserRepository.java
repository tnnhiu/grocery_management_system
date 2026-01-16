package com.tnnhiu.grocery_management_server.modules.identity.repository;

import com.tnnhiu.grocery_management_server.modules.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);
}
