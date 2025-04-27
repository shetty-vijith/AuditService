package com.audit.repository;

import com.audit.entity.AuditConfig;
import org.springframework.stereotype.Component;

import static com.audit.entity.AuditConfig.DEFAULT_ROLLOVER_IN_DAYS;

@Component
public class LocalAuditConfigRepository {
    private final AuditConfig config = new AuditConfig(DEFAULT_ROLLOVER_IN_DAYS);

    public AuditConfig get() {
        return config;
    }

    public AuditConfig update(AuditConfig config) {
        this.config.setRolloverInDays(config.getRolloverInDays());
        return config;
    }
}
