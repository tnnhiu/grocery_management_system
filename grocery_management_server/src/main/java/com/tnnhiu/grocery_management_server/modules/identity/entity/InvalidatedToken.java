package com.tnnhiu.grocery_management_server.modules.identity.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("invalidate_tokens")
public class InvalidatedToken implements Serializable {

    @Id
    String jwtId;

    @TimeToLive
    Long ttl;
}
