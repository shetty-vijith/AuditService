package com.audit.repository;

import com.audit.entity.Role;
import com.audit.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class LocalUserRepository {
    private final List<User> users;

    public LocalUserRepository() {
        users = new ArrayList<>();
        users.add(User
                .builder()
                .id("user1")
                .name("User 1")
                .email("user1@testmail.com")
                .role(Role.ADMIN)
                .build());
        users.add(User
                .builder()
                .id("user2")
                .name("User 2")
                .email("user2@testmail.com")
                .role(Role.SALES)
                .build());
        users.add(User
                .builder()
                .id("user3")
                .name("User 3")
                .email("user3@testmail.com")
                .role(Role.DEVELOPER)
                .build());
    }

    public List<User> getAllUsers() {
        return users;
    }

    public Optional<User> getUser(String id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst();
    }
}
