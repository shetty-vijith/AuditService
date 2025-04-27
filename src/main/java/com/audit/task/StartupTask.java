package com.audit.task;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.ExistsAliasRequest;
import co.elastic.clients.elasticsearch.indices.PutAliasRequest;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.audit.config.ElasticSearchProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StartupTask {
    @Autowired
    ElasticsearchClient elasticsearchClient;
    @Autowired
    ElasticSearchProperties elasticSearchProperties;

    @PostConstruct
    public void init() {
       try {
           String alias = elasticSearchProperties.getAlias();
           String index = elasticSearchProperties.getIndexPrefix() + "0";
           ExistsAliasRequest existsAliasRequest = new ExistsAliasRequest
                   .Builder()
                   .name(alias)
                   .build();
           BooleanResponse existsAliasResponse =
                   elasticsearchClient.indices().existsAlias(existsAliasRequest);

           if (!existsAliasResponse.value()) {
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
           }

       } catch (Exception e) {
           log.error("Error creating the alias for audit event {0}", e);
       }
    }
}
