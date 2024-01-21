package com.tortora.financas.integration;

import com.tortora.financas.exceptions.CustomerNotFoundException;
import com.tortora.financas.model.Customer;
import com.tortora.financas.service.CustomerService;
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
public class CustomerIntegrationTest {

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
    private CustomerService service;

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
        service.deleteAllCustomers();
    }

    @Test
    @Order(1)
    void haveToSaveACustomer() {
        Customer newCustomer = new Customer("FirstNameTest", "LastNameTest");
        EntityModel<Customer> savedCustomer = service.saveCustomer(newCustomer);

        Assertions.assertEquals(1, Objects.requireNonNull(savedCustomer.getContent()).getId());
        Assertions.assertEquals("FirstNameTest", savedCustomer.getContent().getFirstName());
        Assertions.assertEquals("LastNameTest", savedCustomer.getContent().getLastName());
    }

    @Test
    @Order(2)
    void haveToGetAllCustomers() {
        service.saveCustomer(new Customer("FirstNameTest", "LastNameTest"));
        service.saveCustomer(new Customer("FirstNameTest2", "LastNameTest2"));
        service.saveCustomer(new Customer("FirstNameTest3", "LastNameTest3"));

        Assertions.assertEquals(3, service.getCustomers().size());
    }

    @Test
    @Order(3)
    void haveToGetCustomerById() {
        EntityModel<Customer> savedCustomer = service.saveCustomer(new Customer("FirstName", "LastName"));
        EntityModel<Customer> currentCustomer = service.getCustomerById(Objects.requireNonNull(savedCustomer.getContent()).getId());

        Assertions.assertEquals(savedCustomer.getContent().getId(), Objects.requireNonNull(currentCustomer.getContent()).getId());
        Assertions.assertEquals("FirstName", currentCustomer.getContent().getFirstName());
        Assertions.assertEquals("LastName", currentCustomer.getContent().getLastName());
    }

    @Test
    @Order(4)
    void haveToGetCustomersByLastname() {
        service.saveCustomer(new Customer("FirstNameTest", "LastNameTest"));
        service.saveCustomer(new Customer("FirstNameTest2", "LastNameTest"));
        service.saveCustomer(new Customer("FirstNameTest3", "LastNameTest"));

        List<EntityModel<Customer>> customerList = service.getCustomersByLastName("LastNameTest");

        Assertions.assertEquals(3, customerList.size());
    }

    @Test
    @Order(5)
    void haveToDeleteACustomer() {
        EntityModel<Customer> savedCustomer = service.saveCustomer(new Customer("FirstNameTest", "LastNameTest"));
        service.deleteCustomerById(Objects.requireNonNull(savedCustomer.getContent()).getId());

        Assertions.assertThrows(CustomerNotFoundException.class, () -> service.getCustomerById(savedCustomer.getContent().getId()));
    }

}
