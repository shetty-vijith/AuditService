package com.audit.task;

import com.audit.service.ElasticSearchService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StartupTask {
    @Autowired
    ElasticSearchService elasticSearchService;

    @PostConstruct
    public void init() {
        elasticSearchService.createAliasIfAbsent();
    }
}
