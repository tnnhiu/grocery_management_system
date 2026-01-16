package com.tnnhiu.grocery_management_server.modules.identity.repository;

import com.tnnhiu.grocery_management_server.modules.identity.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
