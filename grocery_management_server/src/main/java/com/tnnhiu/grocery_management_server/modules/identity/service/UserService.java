package com.tnnhiu.grocery_management_server.modules.identity.service;


import com.tnnhiu.grocery_management_server.modules.identity.dto.request.UserCreateRequest;

public interface UserService {

    void createUser(UserCreateRequest request);

}
