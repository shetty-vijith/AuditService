package com.audit.service;

import com.audit.entity.AccessEntity;
import com.audit.entity.AuditMessageEntity;
import com.audit.entity.Role;
import com.audit.entity.User;
import com.audit.exception.RoleNotFoundException;
import com.audit.exception.UserNotFoundException;
import com.audit.model.AuditMessage;
import com.audit.repository.AuditRepository;
import com.audit.repository.LocalAccessEntityRepository;
import com.audit.repository.LocalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuditService {
    @Autowired
    private AuditRepository auditRepository;
    @Autowired
    private LocalUserRepository userRepository;
    @Autowired
    private LocalAccessEntityRepository accessRepository;

    public List<AuditMessage> getAuditMessages(String userId) {
        Optional<User> user = userRepository.getUser(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        Role role = user.get().getRole();
        Optional<AccessEntity> accessEntity = accessRepository.getAccessEntity(role);
        if (accessEntity.isEmpty()) {
            throw new RoleNotFoundException();
        }

        return auditRepository.findByNameIn(accessEntity.get().getEventTypes())
                .stream()
                .map(entity -> {
            return AuditMessage
                    .builder()
                    .time(LocalDateTime.ofInstant(Instant.ofEpochSecond(entity.getTime()),
                            ZoneOffset.UTC))
                    .name(entity.getName())
                    .actor(entity.getActor())
                    .data(entity.getData())
                    .build();
        }).collect(Collectors.toList());
    }

    public void save(AuditMessage auditMessage) {
        AuditMessageEntity entity = AuditMessageEntity
                .builder()
                .time(auditMessage.getTime().toEpochSecond(ZoneOffset.UTC))
                .name(auditMessage.getName())
                .actor(auditMessage.getActor())
                .data(auditMessage.getData())
                .build();
        auditRepository.save(entity);
    }
}
