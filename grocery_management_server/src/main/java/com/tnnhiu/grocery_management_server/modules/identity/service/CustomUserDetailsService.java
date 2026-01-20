package com.tnnhiu.grocery_management_server.modules.identity.service;



import com.tnnhiu.grocery_management_server.common.exception.AppException;
import com.tnnhiu.grocery_management_server.modules.identity.entity.User;
import com.tnnhiu.grocery_management_server.modules.identity.entity.UserPrincipal;
import com.tnnhiu.grocery_management_server.modules.identity.exception.IdentityErrorCode;
import com.tnnhiu.grocery_management_server.modules.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new AppException(IdentityErrorCode.USER_NOT_FOUND));
        if (!user.isEnabled()) throw new AppException(IdentityErrorCode.USER_NOT_FOUND);
        return new UserPrincipal(user);
    }
}
