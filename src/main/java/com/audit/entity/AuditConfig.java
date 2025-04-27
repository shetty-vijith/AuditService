package com.audit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditConfig {
    public final static Integer DEFAULT_ROLLOVER_IN_DAYS = 7;
    private Integer rolloverInDays;
}
