package com.audit.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

import java.io.IOException;

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {
    @Autowired
    ElasticSearchProperties properties;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration
                .builder()
                .connectedTo(properties.getHost() + ":" + properties.getPort())
                .build();
    }

    @Bean
    ElasticsearchClient elasticsearchClient() throws IOException {
        try (RestClient restClient = RestClient.builder(
                new HttpHost(properties.getHost(), Integer.parseInt(properties.getPort()))).build()) {
            JacksonJsonpMapper jsonMapper = new JacksonJsonpMapper();
            ElasticsearchTransport elasticsearchTransport =
                    new RestClientTransport(restClient, jsonMapper);
            return new ElasticsearchClient(elasticsearchTransport);
        }
    }
}
