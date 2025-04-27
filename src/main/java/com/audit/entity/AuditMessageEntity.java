package com.audit.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "#{@elasticSearchProperties.getAlias()}", createIndex = false)
@Builder
@Getter
public class AuditMessageEntity {
    @Id
    private String id;
    private Long time;
    private EventTypes name;
    private Actor actor;
    private EventData data;
}
