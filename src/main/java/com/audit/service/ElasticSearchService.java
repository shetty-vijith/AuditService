package com.audit.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.ExistsAliasRequest;
import co.elastic.clients.elasticsearch.indices.PutAliasRequest;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.audit.config.ElasticSearchProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ElasticSearchService {
    @Autowired
    ElasticsearchClient elasticsearchClient;
    @Autowired
    ElasticSearchProperties elasticSearchProperties;

    public void createAliasIfAbsent() {
        String index = elasticSearchProperties.getIndexPrefix() + "0";
        String alias = elasticSearchProperties.getAlias();

        if (!existsAlias(alias)) {
            try {
                CreateIndexRequest createIndexRequest = new CreateIndexRequest
                        .Builder()
                        .index(index)
                        .build();
                elasticsearchClient.indices().create(createIndexRequest);

                PutAliasRequest putAliasRequest = new PutAliasRequest
                        .Builder()
                        .index(index)
                        .name(elasticSearchProperties.getAlias())
                        .build();
                elasticsearchClient.indices().putAlias(putAliasRequest);
            } catch (Exception e) {
                log.error("Error creating index/alias {}", e.getMessage(), e);
            }
        }
    }
    public boolean existsAlias(String alias) {
        try {
            ExistsAliasRequest existsAliasRequest = new ExistsAliasRequest
                    .Builder()
                    .name(alias)
                    .build();
            BooleanResponse existsAliasResponse =
                    elasticsearchClient.indices().existsAlias(existsAliasRequest);
            return existsAliasResponse.value();
        } catch (Exception e) {
            log.error("Error checking if alias exist {}", e.getMessage(), e);
        }
        return false;
    }
}
