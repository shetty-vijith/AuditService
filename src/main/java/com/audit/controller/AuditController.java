package com.audit.controller;

import com.audit.model.AuditMessage;
import com.audit.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/audit")
public class AuditController {
    @Autowired
    AuditService auditService;

    @RequestMapping("/messages")
    List<AuditMessage> getAllMessages(@RequestParam("userId") String userId) {
        return auditService.getAuditMessages(userId);
    }
}
