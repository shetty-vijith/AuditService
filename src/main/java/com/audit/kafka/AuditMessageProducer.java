package com.audit.kafka;

import com.audit.model.AuditMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuditMessageProducer {
    @Autowired
    private KafkaTemplate<String, AuditMessage> kafkaTemplate;

    public void send(String topic, AuditMessage message) {
        kafkaTemplate.send(topic, message);
    }
}