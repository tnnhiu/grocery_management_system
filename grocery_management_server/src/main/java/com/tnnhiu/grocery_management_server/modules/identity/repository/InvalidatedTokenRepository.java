package com.tnnhiu.grocery_management_server.modules.identity.repository;

import com.tnnhiu.grocery_management_server.modules.identity.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends CrudRepository<InvalidatedToken, String> {
}
