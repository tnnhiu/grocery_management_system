package com.tnnhiu.grocery_management_server.modules.identity.controller;

import com.tnnhiu.grocery_management_server.modules.identity.dto.request.UserCreateRequest;
import com.tnnhiu.grocery_management_server.modules.identity.dto.response.UserCreateResponse;
import com.tnnhiu.grocery_management_server.modules.identity.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    // create
    @PostMapping
    public ResponseEntity<UserCreateResponse> createUser(
            @RequestBody @Valid UserCreateRequest request
    ) {
        return ResponseEntity.ok(userService.createUser(request));
    }
    // update
    // delete
}
