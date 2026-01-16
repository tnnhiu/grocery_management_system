package com.tnnhiu.grocery_management_server.modules.identity.dto.response;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateResponse {
    private String id;
    private String username;
}
