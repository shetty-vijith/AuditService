package com.audit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "audit.elasticsearch")
@Getter
@Setter
public class ElasticSearchProperties {
    private String host;
    private String port;
    private String alias;
    private String indexPrefix;
}
