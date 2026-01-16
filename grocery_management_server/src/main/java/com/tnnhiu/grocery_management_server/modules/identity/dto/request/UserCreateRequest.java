package com.tnnhiu.grocery_management_server.modules.identity.dto.request;

import lombok.Getter;

@Getter
public class UserCreateRequest {
    private String username;
    private String password;
}
