package com.audit.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch.indices.RolloverRequest;
import co.elastic.clients.elasticsearch.indices.RolloverResponse;
import co.elastic.clients.elasticsearch.indices.rollover.RolloverConditions;
import com.audit.config.ElasticSearchProperties;
import com.audit.entity.AuditConfig;
import com.audit.repository.LocalAuditConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuditConfigService {
    @Autowired
    ElasticsearchClient elasticsearchClient;
    @Autowired
    ElasticSearchProperties elasticSearchProperties;
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
        try {
            RolloverConditions.Builder rolloverCondition =
                    new RolloverConditions.Builder();
            rolloverCondition.maxAgeMillis(TimeUnit.DAYS.toMillis(rolloverInDays));
            RolloverRequest rolloverRequest = new RolloverRequest
                    .Builder()
                    .alias(elasticSearchProperties.getAlias())
                    .conditions(rolloverCondition.build())
                    .build();
            elasticsearchClient.indices().rollover(rolloverRequest);
        } catch (Exception e) {
            log.error("Error performing rollover {}", e.getMessage(), e);
        }
        return updatedConfig;
    }
}
