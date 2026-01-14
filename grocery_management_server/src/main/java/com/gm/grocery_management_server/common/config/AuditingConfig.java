package com.gm.grocery_management_server.common.config;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

@Component
@EnableJpaAuditing
public class AuditingConfig {
}
