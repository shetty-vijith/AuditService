package com.audit.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.elasticsearch.indices.RolloverRequest;
import com.audit.config.ElasticSearchProperties;
import com.audit.entity.*;
import com.audit.repository.LocalAuditConfigRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuditConfigServiceTest {
    @Mock
    ElasticsearchClient elasticsearchClient;
    @Mock
    ElasticSearchProperties elasticSearchProperties;
    @Mock
    ElasticsearchIndicesClient indicesClient;
    @Mock
    LocalAuditConfigRepository auditConfigRepository;
    @InjectMocks
    AuditConfigService auditConfigService;

    @Test
    public void getConfig() {
        AuditConfig config = AuditConfig
                .builder()
                .rolloverInDays(7)
                .build();
        when(auditConfigRepository.get()).thenReturn(config);
        AuditConfig returnConfig = auditConfigService.getConfig();
        Assertions.assertEquals(7, returnConfig.getRolloverInDays());
    }

    @Test
    public void updateConfig() throws IOException {
        when(elasticSearchProperties.getAlias()).thenReturn("testAlias");
        when(elasticsearchClient.indices()).thenReturn(indicesClient);
        auditConfigService.updateConfig(10);

        ArgumentCaptor<AuditConfig> configCaptor = ArgumentCaptor.forClass(AuditConfig.class);
        verify(auditConfigRepository).update(configCaptor.capture());
        Assertions.assertEquals(10, configCaptor.getValue().getRolloverInDays());

        ArgumentCaptor<RolloverRequest> rolloverCaptor = ArgumentCaptor.forClass(RolloverRequest.class);
        verify(indicesClient).rollover(rolloverCaptor.capture());
        RolloverRequest rolloverRequest = rolloverCaptor.getValue();
        Assertions.assertEquals("testAlias", rolloverRequest.alias());
        Assertions.assertEquals(TimeUnit.DAYS.toMillis(10), rolloverRequest.conditions().maxAgeMillis());
    }

}
