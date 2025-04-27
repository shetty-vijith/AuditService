package com.audit.repository;

import com.audit.entity.AccessEntity;
import com.audit.entity.EventTypes;
import com.audit.entity.Role;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class LocalAccessEntityRepository {
    public final List<AccessEntity> accessEntities;

    public LocalAccessEntityRepository() {
        accessEntities = new ArrayList<>();
        accessEntities.add(AccessEntity
                .builder()
                .role(Role.ADMIN)
                .eventTypes(Arrays.asList(EventTypes.LOGIN, EventTypes.SCAN, EventTypes.GET_CLIENT_INFO))
                .build());
        accessEntities.add(AccessEntity
                .builder()
                .role(Role.SALES)
                .eventTypes(List.of(EventTypes.GET_CLIENT_INFO))
                .build());
        accessEntities.add(AccessEntity
                .builder()
                .role(Role.DEVELOPER)
                .eventTypes(Arrays.asList(EventTypes.LOGIN, EventTypes.SCAN))
                .build());
    }

    public Optional<AccessEntity> getAccessEntity(Role role) {
        return accessEntities.stream()
                .filter(accessEntity -> accessEntity.getRole() == role)
                .findFirst();
    }
}
