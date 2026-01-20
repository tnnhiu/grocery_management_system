package com.tnnhiu.grocery_management_server.modules.identity.controller;


import com.tnnhiu.grocery_management_server.modules.identity.dto.request.LoginRequest;
import com.tnnhiu.grocery_management_server.modules.identity.dto.request.LogoutRequest;
import com.tnnhiu.grocery_management_server.modules.identity.dto.request.RefreshTokenRequest;
import com.tnnhiu.grocery_management_server.modules.identity.dto.request.UserCreateRequest;
import com.tnnhiu.grocery_management_server.modules.identity.dto.response.LoginResponse;
import com.tnnhiu.grocery_management_server.modules.identity.dto.response.RefreshTokenResponse;
import com.tnnhiu.grocery_management_server.modules.identity.dto.response.UserCreateResponse;
import com.tnnhiu.grocery_management_server.modules.identity.service.AuthenticationService;
import com.tnnhiu.grocery_management_server.modules.identity.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<UserCreateResponse> register(
            @RequestBody @Valid UserCreateRequest request
    ) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @RequestBody @Valid RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid LogoutRequest request) {
        authenticationService.logout(request);
        return ResponseEntity.ok().build();
    }
}
