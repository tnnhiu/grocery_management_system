package com.tnnhiu.grocery_management_server.common.configuration;
import com.tnnhiu.grocery_management_server.common.exception.AppException;
import com.tnnhiu.grocery_management_server.modules.identity.enums.TokenTypeEnum;
import com.tnnhiu.grocery_management_server.modules.identity.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class JwtDecoderConfiguration {

    private final JwtService jwtService;

    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> {
            try {
                Jwt jwt = jwtService.verifyToken(token);

                String type = jwt.getClaim("type");
                if (!TokenTypeEnum.ACCESS.name().equals(type)) {
                    throw new JwtException("Invalid token type");
                }

                return jwt;
            } catch (AppException e) {
                throw new JwtException(e.getErrorCode().getMessage());
            } catch (Exception e) {
                throw new JwtException(e.getMessage());
            }
        };
    }
}
