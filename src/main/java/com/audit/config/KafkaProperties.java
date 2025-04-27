package com.audit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "audit.kafka")
@Getter
@Setter
public class KafkaProperties {
    private String host;
    private String port;
    private String groupId;
    private String topic;
}
