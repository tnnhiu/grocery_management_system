package com.tnnhiu.grocery_management_server.modules.identity.service;

import com.tnnhiu.grocery_management_server.modules.identity.dto.request.UserCreateRequest;
import com.tnnhiu.grocery_management_server.modules.identity.repository.RoleRepository;
import com.tnnhiu.grocery_management_server.modules.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void createUser(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
    }
}
