package com.audit.controller;

import com.audit.config.KafkaProperties;
import com.audit.entity.Actor;
import com.audit.model.AuditMessage;
import com.audit.entity.EventData;
import com.audit.entity.EventTypes;
import com.audit.kafka.AuditMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/v1/notification")
public class NotificationController {
    @Autowired
    AuditMessageProducer auditMessageProducer;
    @Autowired
    KafkaProperties kafkaProperties;

    @RequestMapping("/login")
    void generateLoginEvent() {
        AuditMessage loginMessage = AuditMessage
                .builder()
                .time(LocalDateTime.now())
                .name(EventTypes.LOGIN)
                .actor(Actor.builder().userId("testUser").build())
                .data(EventData.builder().data(Map.of("type", EventTypes.LOGIN.name())).build())
                .build();
        auditMessageProducer.send(kafkaProperties.getTopic(), loginMessage);
    }

    @RequestMapping("/getClientInfo")
    void generateClientInfo() {
        AuditMessage clientInfoMessage = AuditMessage
                .builder()
                .time(LocalDateTime.now())
                .name(EventTypes.GET_CLIENT_INFO)
                .actor(Actor.builder().userId("testUser").build())
                .data(EventData.builder().data(Map.of("type", EventTypes.GET_CLIENT_INFO.name())).build())
                .build();
        auditMessageProducer.send(kafkaProperties.getTopic(), clientInfoMessage);
    }

    @RequestMapping("/scan")
    void generateScan() {
        AuditMessage scanMessage = AuditMessage
                .builder()
                .time(LocalDateTime.now())
                .name(EventTypes.SCAN)
                .actor(Actor.builder().userId("testUser").build())
                .data(EventData.builder().data(Map.of("type", EventTypes.SCAN.name())).build())
                .build();
        auditMessageProducer.send(kafkaProperties.getTopic(), scanMessage);
    }

}
