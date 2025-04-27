package com.audit.service;

import com.audit.entity.*;
import com.audit.exception.RoleNotFoundException;
import com.audit.exception.UserNotFoundException;
import com.audit.model.AuditMessage;
import com.audit.repository.AuditRepository;
import com.audit.repository.LocalAccessEntityRepository;
import com.audit.repository.LocalUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuditServiceTest {
    @Mock
    AuditRepository auditRepository;
    @Mock
    LocalUserRepository localUserRepository;
    @Mock
    LocalAccessEntityRepository localAccessEntityRepository;
    @InjectMocks
    AuditService auditService;

    @Test
    public void userNotFound() {
        when(localUserRepository.getUser("user1")).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class,
                () -> auditService.getAuditMessages("user1"));
    }

    @Test
    public void roleNotFound() {
        User user = User
                .builder()
                .id("user2")
                .role(Role.SALES)
                .email("user2@test.com")
                .build();
        when(localUserRepository.getUser("user2")).thenReturn(Optional.of(user));
        when(localAccessEntityRepository.getAccessEntity(Role.SALES))
                .thenThrow(RoleNotFoundException.class);

        Assertions.assertThrows(RoleNotFoundException.class,
                () -> auditService.getAuditMessages("user2"));
    }

    @Test
    public void getAllMessages() {
        User user = User
                .builder()
                .id("user3")
                .role(Role.SALES)
                .email("user2@test.com")
                .build();
        when(localUserRepository.getUser("user3")).thenReturn(Optional.of(user));

        AccessEntity accessEntity = AccessEntity
                .builder()
                .role(Role.SALES)
                .eventTypes(List.of(EventTypes.GET_CLIENT_INFO))
                .build();
        when(localAccessEntityRepository.getAccessEntity(Role.SALES))
                .thenReturn(Optional.of(accessEntity));

        AuditMessageEntity entity = AuditMessageEntity
                .builder()
                .id("testMessage")
                .time(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                .name(EventTypes.GET_CLIENT_INFO)
                .actor(Actor
                        .builder()
                        .userId("testUser3")
                        .build())
                .data(EventData
                        .builder()
                        .data(Map.of("key", "value"))
                        .build())
                .build();
        when(auditRepository.findByNameIn(List.of(EventTypes.GET_CLIENT_INFO)))
                .thenReturn(List.of(entity));

       List<AuditMessage> auditMessages = auditService.getAuditMessages("user3");
       Assertions.assertEquals(EventTypes.GET_CLIENT_INFO, auditMessages.get(0).getName());
       Assertions.assertEquals("testUser3", auditMessages.get(0).getActor().getUserId());
    }
}
