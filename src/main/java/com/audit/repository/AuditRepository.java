package com.audit.repository;

import com.audit.entity.AuditMessageEntity;
import com.audit.entity.EventTypes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditRepository extends ElasticsearchRepository<AuditMessageEntity, String> {
    List<AuditMessageEntity> findByTimeBefore(Long until);
    List<AuditMessageEntity> findByNameIn(List<EventTypes> names);
}
