package com.audit.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.audit.config.ElasticSearchProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ElasticSearchServiceTest {
    @Mock
    ElasticsearchClient elasticsearchClient;
    @Mock
    ElasticSearchProperties elasticSearchProperties;
    @Mock
    ElasticsearchIndicesClient indicesClient;
    @InjectMocks
    ElasticSearchService elasticSearchService;

    @BeforeEach
    public void setup() {
        when(elasticsearchClient.indices()).thenReturn(indicesClient);
    }

    @Test
    public void existAlias() throws IOException {
        when(indicesClient.existsAlias(any(ExistsAliasRequest.class))).thenReturn(new BooleanResponse(true));
        Assertions.assertTrue(elasticSearchService.existsAlias("testAlias"));
    }

    @Test
    public void existAliasNot() throws IOException {
        when(indicesClient.existsAlias(any(ExistsAliasRequest.class))).thenReturn(new BooleanResponse(false));
        Assertions.assertFalse(elasticSearchService.existsAlias("testAlias"));
    }

    @Test
    public void doNotCreateAliasIfPresent() throws IOException {
        when(indicesClient.existsAlias(any(ExistsAliasRequest.class))).thenReturn(new BooleanResponse(true));
        when(elasticSearchProperties.getAlias()).thenReturn("testAlias");
        when(elasticSearchProperties.getIndexPrefix()).thenReturn("testIndexPrefix-");

        elasticSearchService.createAliasIfAbsent();
        verify(indicesClient, never()).create(any(CreateIndexRequest.class));
        verify(indicesClient, never()).putAlias(any(PutAliasRequest.class));
    }

    @Test
    public void createAliasIfAbsent() throws IOException {
        when(indicesClient.existsAlias(any(ExistsAliasRequest.class))).thenReturn(new BooleanResponse(false));
        when(elasticSearchProperties.getAlias()).thenReturn("testAlias");
        when(elasticSearchProperties.getIndexPrefix()).thenReturn("testIndexPrefix-");

        elasticSearchService.createAliasIfAbsent();
        ArgumentCaptor<CreateIndexRequest> captorIndex = ArgumentCaptor.forClass(CreateIndexRequest.class);
        ArgumentCaptor<PutAliasRequest> captorAlias = ArgumentCaptor.forClass(PutAliasRequest.class);

        verify(indicesClient).create(captorIndex.capture());
        verify(indicesClient).putAlias(captorAlias.capture());

        Assertions.assertEquals("testIndexPrefix-0", captorIndex.getValue().index());
        Assertions.assertEquals("testAlias", captorAlias.getValue().name());
    }

    @Test
    public void rollover() throws IOException {
        when(elasticSearchProperties.getAlias()).thenReturn("testAlias");
        elasticSearchService.rollover(5);

        ArgumentCaptor<RolloverRequest> captorRollover = ArgumentCaptor.forClass(RolloverRequest.class);
        verify(indicesClient).rollover(captorRollover.capture());

        RolloverRequest rolloverRequest = captorRollover.getValue();
        Assertions.assertEquals("testAlias", rolloverRequest.alias());
        Assertions.assertEquals(TimeUnit.DAYS.toMillis(5), rolloverRequest.conditions().maxAgeMillis());
    }
}
