package com.tortora.financas.integration;

import com.tortora.financas.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Collection;

@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserIntegrationTest {

    @Container
    static MySQLContainer mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0-debian"));

    @DynamicPropertySource
    static void mySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> mySQLContainer.getJdbcUrl());
        registry.add("spring.datasource.driverClassName", () -> mySQLContainer.getDriverClassName());
        registry.add("spring.datasource.username", () -> mySQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> mySQLContainer.getPassword());
    }

    @Autowired
    private UserService service;

    @BeforeAll
    static void startContainers() {
        mySQLContainer.start();
    }

    @AfterAll
    static void stopContainers() {
        mySQLContainer.stop();
    }

    @AfterEach
    void setUp() {
        service.deleteUsers();
    }

    @Test
    void haveToSaveUser() {
        int afterSave = ((Collection<?>) service.getUsers()).size();
        service.saveUser("Filipe", "fedispato@gmail.com");
        int beforeSave = ((Collection<?>) service.getUsers()).size();

        Assertions.assertEquals(1, beforeSave - afterSave);
    }

    @Test
    void haveToGetAllUsers() {
        service.saveUser("Filipe", "fedispato@gmail.com");
        service.saveUser("Debora", "debora@gmail.com");
        service.saveUser("Best", "best@gmail.com");
        int numberOfUsers = ((Collection<?>) service.getUsers()).size();

        Assertions.assertEquals(3, numberOfUsers);
    }

    @Test
    void haveToDeleteAllUsers() {
        service.saveUser("Filipe", "fedispato@gmail.com");
        service.saveUser("Debora", "debora@gmail.com");
        service.saveUser("Best", "best@gmail.com");
        service.deleteUsers();

        int numberOfUsers = ((Collection<?>) service.getUsers()).size();

        Assertions.assertEquals(0, numberOfUsers);
    }

}
