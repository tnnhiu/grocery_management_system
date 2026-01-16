package com.tnnhiu.grocery_management_server.modules.identity.service;


import com.tnnhiu.grocery_management_server.modules.identity.dto.request.UserCreateRequest;
import com.tnnhiu.grocery_management_server.modules.identity.dto.response.UserCreateResponse;

public interface UserService {
    UserCreateResponse createUser(UserCreateRequest request);
}
