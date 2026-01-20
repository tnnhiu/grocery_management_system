package com.tnnhiu.grocery_management_server.modules.identity.service;

import com.tnnhiu.grocery_management_server.common.exception.AppException;
import com.tnnhiu.grocery_management_server.modules.identity.dto.request.LoginRequest;
import com.tnnhiu.grocery_management_server.modules.identity.dto.request.LogoutRequest;
import com.tnnhiu.grocery_management_server.modules.identity.dto.request.RefreshTokenRequest;
import com.tnnhiu.grocery_management_server.modules.identity.dto.response.LoginResponse;
import com.tnnhiu.grocery_management_server.modules.identity.dto.response.RefreshTokenResponse;
import com.tnnhiu.grocery_management_server.modules.identity.entity.User;
import com.tnnhiu.grocery_management_server.modules.identity.entity.UserPrincipal;
import com.tnnhiu.grocery_management_server.modules.identity.enums.TokenTypeEnum;
import com.tnnhiu.grocery_management_server.modules.identity.exception.IdentityErrorCode;
import com.tnnhiu.grocery_management_server.modules.identity.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    AuthenticationManager authenticationManager;
    JwtService jwtService;
    UserRepository userRepository;

    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        log.info("Checking login");
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        log.info("Done!");
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();

        return LoginResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }

    public void logout(LogoutRequest request) {
        jwtService.invalidateToken(request.getToken());
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        Jwt jwt = jwtService.verifyToken(request.getRefreshToken());

        String type = jwt.getClaim("type");
        if (!TokenTypeEnum.REFRESH.name().equals(type)) {
            throw new AppException(IdentityErrorCode.JWT_INVALID);
        }

        // Rotate token: invalidate old one
        jwtService.invalidateToken(request.getRefreshToken());

        String username = jwt.getSubject();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new AppException(IdentityErrorCode.USER_NOT_FOUND));

        if (!user.isEnabled()) {
            throw new AppException(IdentityErrorCode.UNAUTHENTICATED);
        }

        return RefreshTokenResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }
}
