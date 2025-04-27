package com.audit.controller;

import com.audit.entity.Actor;
import com.audit.entity.EventData;
import com.audit.entity.EventTypes;
import com.audit.exception.RoleNotFoundException;
import com.audit.exception.UserNotFoundException;
import com.audit.model.AuditMessage;
import com.audit.service.AuditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
public class AuditControllerTest {
    private final static ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    @Mock
    AuditService auditService;
    @InjectMocks
    AuditController auditController;

    @BeforeEach
    public void setup() {
        mapper.findAndRegisterModules();
        mvc = MockMvcBuilders.standaloneSetup(auditController).build();
    }

    @Test
    public void getAllMessages() throws Exception {
        List<AuditMessage> auditMessages = Collections.singletonList(AuditMessage
                .builder()
                .name(EventTypes.LOGIN)
                .time(LocalDateTime.now())
                .actor(Actor.builder().userId("testUser").build())
                .data(EventData.builder().data(Map.of("key", "value")).build())
                .build());
        when(auditService.getAuditMessages("user1")).thenReturn(auditMessages);

        MockHttpServletResponse mockResponse =  mvc.perform(get("/v1/audit/messages?userId=user1"))
                .andReturn().getResponse();
        AuditMessage[] response = mapper.readValue(mockResponse.getContentAsString(), AuditMessage[].class);

        Assertions.assertEquals(200, mockResponse.getStatus());
        Assertions.assertEquals(EventTypes.LOGIN, response[0].getName());
        Assertions.assertEquals("testUser", response[0].getActor().getUserId());
    }

    @Test
    public void userNotFound() throws Exception {
        when(auditService.getAuditMessages("user2")).thenThrow(UserNotFoundException.class);

        MockHttpServletResponse mockResponse =  mvc.perform(get("/v1/audit/messages?userId=user2"))
                .andReturn().getResponse();

        Assertions.assertEquals(404, mockResponse.getStatus());
    }

    @Test
    public void roleNotFound() throws Exception {
        when(auditService.getAuditMessages("user3")).thenThrow(RoleNotFoundException.class);

        MockHttpServletResponse mockResponse =  mvc.perform(get("/v1/audit/messages?userId=user3"))
                .andReturn().getResponse();

        Assertions.assertEquals(404, mockResponse.getStatus());
    }
}
