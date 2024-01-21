package com.tortora.financas.integration;

import com.tortora.financas.enums.Status;
import com.tortora.financas.model.Order;
import com.tortora.financas.service.OrderService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Objects;

@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderIntegrationTest {

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
    private OrderService service;

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
        service.deleteAllOrders();
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void haveToSaveOrder() {
        EntityModel<Order> savedOrder = service.saveOrder(new Order("Ordem 1", Status.IN_PROGRESS));

        Assertions.assertEquals(1, Objects.requireNonNull(savedOrder.getContent()).getId());
        Assertions.assertEquals("Ordem 1", savedOrder.getContent().getDescription());
        Assertions.assertEquals(Status.IN_PROGRESS, savedOrder.getContent().getStatus());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void haveToGetAllOrders() {
        service.saveOrder(new Order("Ordem 1", Status.IN_PROGRESS));
        service.saveOrder(new Order("Ordem 2", Status.IN_PROGRESS));
        service.saveOrder(new Order("Ordem 3", Status.IN_PROGRESS));
        List<EntityModel<Order>> orderList = service.getOrders();

        Assertions.assertEquals(3, orderList.size());
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void haveToGetOrderById() {
        EntityModel<Order> savedOrder = service.saveOrder(new Order("Ordem 1", Status.IN_PROGRESS));
        Order order = service.getOrderById(Objects.requireNonNull(savedOrder.getContent()).getId());

        Assertions.assertEquals(savedOrder.getContent().getId(), order.getId());
        Assertions.assertEquals("Ordem 1", order.getDescription());
        Assertions.assertEquals(Status.IN_PROGRESS, order.getStatus());
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void haveToGetEntityModelOrderById() {
        EntityModel<Order> savedOrder = service.saveOrder(new Order("Ordem 1", Status.IN_PROGRESS));
        EntityModel<Order> order = service.getEntityModelOrderById(Objects.requireNonNull(savedOrder.getContent()).getId());

        Assertions.assertEquals(savedOrder.getContent().getId(), Objects.requireNonNull(order.getContent()).getId());
        Assertions.assertEquals("Ordem 1", order.getContent().getDescription());
        Assertions.assertEquals(Status.IN_PROGRESS, order.getContent().getStatus());
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    void haveToCancelAnOrder() {
        EntityModel<Order> savedOrder = service.saveOrder(new Order("Ordem 1", Status.IN_PROGRESS));
        service.cancelOrder(Objects.requireNonNull(savedOrder.getContent()));
        Order currentOrder = service.getOrderById(savedOrder.getContent().getId());

        Assertions.assertEquals(Status.CANCELLED, currentOrder.getStatus());
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    void haveToCompleteAnOrder() {
        EntityModel<Order> savedOrder = service.saveOrder(new Order("Ordem 1", Status.IN_PROGRESS));
        service.completeOrder(Objects.requireNonNull(savedOrder.getContent()));
        Order currentOrder = service.getOrderById(savedOrder.getContent().getId());

        Assertions.assertEquals(Status.COMPLETED, currentOrder.getStatus());
    }

}
