package com.youngxpepp.instagramcloneserver.global.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youngxpepp.instagramcloneserver.Application;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Disabled
public abstract class IntegrationTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}
