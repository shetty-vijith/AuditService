package com.audit.kafka;

import com.audit.model.AuditMessage;
import com.audit.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AuditMessageConsumer {
    @Autowired
    AuditService auditService;

    @KafkaListener(topics = "${audit.kafka.topic}", groupId = "${audit.kafka.groupId}")
    public void consume(AuditMessage message) {
        auditService.save(message);
    }
}
