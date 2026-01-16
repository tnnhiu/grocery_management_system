package com.tnnhiu.grocery_management_server.modules.identity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;

@Setter
@Builder
@AllArgsConstructor
public class UserCreateResponse {
    private String username;
}
