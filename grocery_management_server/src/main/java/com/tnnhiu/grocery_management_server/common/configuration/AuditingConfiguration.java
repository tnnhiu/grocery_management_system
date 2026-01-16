package com.tnnhiu.grocery_management_server.common.configuration;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

@Component
@EnableJpaAuditing
public class AuditingConfiguration {
}
