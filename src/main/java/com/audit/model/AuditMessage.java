package com.audit.model;

import com.audit.entity.Actor;
import com.audit.entity.EventData;
import com.audit.entity.EventTypes;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditMessage {
    private LocalDateTime time;
    private EventTypes name;
    private Actor actor;
    private EventData data;
}
