package com.audit.service;

import com.audit.entity.*;
import com.audit.repository.LocalAuditConfigRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuditConfigServiceTest {
    @Mock
    ElasticSearchService elasticSearchService;
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
        auditConfigService.updateConfig(10);
        verify(auditConfigRepository).update(AuditConfig.builder().rolloverInDays(10).build());
        verify(elasticSearchService).rollover(10);
    }

}
