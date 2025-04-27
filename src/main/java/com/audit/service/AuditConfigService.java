package com.audit.service;

import com.audit.entity.AuditConfig;
import com.audit.repository.LocalAuditConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuditConfigService {
    @Autowired
    ElasticSearchService elasticSearchService;
    @Autowired
    LocalAuditConfigRepository auditConfigRepository;

    public AuditConfig getConfig() {
        return auditConfigRepository.get();
    }

    public AuditConfig updateConfig(Integer rolloverInDays) {
        AuditConfig config = AuditConfig
                .builder()
                .rolloverInDays(rolloverInDays)
                .build();
        AuditConfig updatedConfig = auditConfigRepository.update(config);
        elasticSearchService.rollover(rolloverInDays);
        return updatedConfig;
    }
}
