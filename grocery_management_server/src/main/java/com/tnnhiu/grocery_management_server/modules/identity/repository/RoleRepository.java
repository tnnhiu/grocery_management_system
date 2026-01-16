package com.tnnhiu.grocery_management_server.modules.identity.repository;

import com.tnnhiu.grocery_management_server.modules.identity.entity.Role;
import com.tnnhiu.grocery_management_server.modules.identity.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(RoleEnum name);
}
