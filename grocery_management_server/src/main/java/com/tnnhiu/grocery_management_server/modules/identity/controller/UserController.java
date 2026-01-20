package com.tnnhiu.grocery_management_server.modules.identity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/greeting")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello World");
    }
    // create
    // update
    // delete
}
