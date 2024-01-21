package com.tortora.financas.integration;

import com.tortora.financas.exceptions.EmployeeNotFoundException;
import com.tortora.financas.model.Employee;
import com.tortora.financas.service.EmployeeService;
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
public class EmployeeIntegrationTest {

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
    private EmployeeService service;

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
        service.deleteAllEmployees();
    }

    @Test
    @Order(1)
    void haveToSaveAnEmployee() {
        Employee employee = new Employee("Filipe", "Tortora", "Programador");

        EntityModel<Employee> response = service.saveEmployee(employee);

        Assertions.assertEquals("Filipe", Objects.requireNonNull(response.getContent()).getFirstName());
        Assertions.assertEquals("Tortora", response.getContent().getLastName());
        Assertions.assertEquals("Programador", response.getContent().getRole());
    }

    @Test
    @Order(2)
    void haveToGetAllEmployees() {
        service.saveEmployee(new Employee("Filipe", "Tortora", "Programador"));
        service.saveEmployee(new Employee("Debora", "Brandao", "Contadora"));
        service.saveEmployee(new Employee("Best", "Sauro", "Cabeleileira"));

        List<EntityModel<Employee>> employees = service.getEmployees();

        Assertions.assertEquals(3, employees.size());
    }

    @Test
    @Order(3)
    void haveToUpdateAnEmployee() {
        Employee employee = new Employee("Filipe", "Tortora", "Programador");
        service.saveEmployee(employee);

        Employee newEmployee = new Employee("Felipe", "Dias", "Analista");
        EntityModel<Employee> updatedEmployee = service.updateEmployee(newEmployee, 1L);

        Assertions.assertEquals("Felipe", Objects.requireNonNull(updatedEmployee.getContent()).getFirstName());
        Assertions.assertEquals("Dias", updatedEmployee.getContent().getLastName());
        Assertions.assertEquals("Analista", updatedEmployee.getContent().getRole());
    }

    @Test
    @Order(4)
    void haveToDeleteAnEmployee() {
        Employee employee = new Employee("Filipe", "Tortora", "Programador");
        EntityModel<Employee> response = service.saveEmployee(employee);
        service.deleteEmployeeById(Objects.requireNonNull(response.getContent()).getId());

        Assertions.assertThrows(EmployeeNotFoundException.class, () -> {
            service.getEmployeeById(1L);
        });
    }

}
