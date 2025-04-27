package com.audit.controller;

import com.audit.entity.AuditConfig;
import com.audit.service.AuditConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/config")
public class AuditConfigController {
    @Autowired
    AuditConfigService auditConfigService;

    @RequestMapping("/")
    AuditConfig getConfig() {
        return auditConfigService.getConfig();
    }

    @PutMapping("/")
    AuditConfig updateConfig(@RequestBody Integer rolloverInDays) {
        return auditConfigService.updateConfig(rolloverInDays);
    }
}
