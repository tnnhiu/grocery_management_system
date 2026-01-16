package com.tnnhiu.grocery_management_server.modules.identity.service;

import com.tnnhiu.grocery_management_server.common.exception.AppException;
import com.tnnhiu.grocery_management_server.modules.identity.dto.request.UserCreateRequest;
import com.tnnhiu.grocery_management_server.modules.identity.dto.response.UserCreateResponse;
import com.tnnhiu.grocery_management_server.modules.identity.entity.Role;
import com.tnnhiu.grocery_management_server.modules.identity.entity.User;
import com.tnnhiu.grocery_management_server.modules.identity.enums.RoleEnum;
import com.tnnhiu.grocery_management_server.modules.identity.exception.IdentityErrorCode;
import com.tnnhiu.grocery_management_server.modules.identity.repository.RoleRepository;
import com.tnnhiu.grocery_management_server.modules.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserCreateResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(IdentityErrorCode.USER_EXISTED);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        Role role = roleRepository.findByName(RoleEnum.STAFF)
                .orElseThrow(() -> new AppException(IdentityErrorCode.ROLE_NOT_FOUND));

        User newUser = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(role)
                .build();

        User savedUser = userRepository.save(newUser);

        return UserCreateResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .build();
    }
}
