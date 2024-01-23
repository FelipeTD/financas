package com.tortora.financas.integration;

import com.tortora.financas.enums.Status;
import com.tortora.financas.exceptions.CustomerNotFoundException;
import com.tortora.financas.exceptions.EmployeeNotFoundException;
import com.tortora.financas.model.Customer;
import com.tortora.financas.model.Employee;
import com.tortora.financas.service.CustomerService;
import com.tortora.financas.service.EmployeeService;
import com.tortora.financas.service.OrderService;
import com.tortora.financas.service.UserService;
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

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTest {

    @Container
    static MySQLContainer mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0-debian"));

    @DynamicPropertySource
    static void mySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> mySQLContainer.getJdbcUrl());
        registry.add("spring.datasource.driverClassName", () -> mySQLContainer.getDriverClassName());
        registry.add("spring.datasource.username", () -> mySQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> mySQLContainer.getPassword());
        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

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
        customerService.deleteAllCustomers();
        employeeService.deleteAllEmployees();
        orderService.deleteAllOrders();
        userService.deleteUsers();
    }

    @Test
    @Order(1)
    void haveToSaveACustomer() {
        Customer newCustomer = new Customer("FirstNameTest", "LastNameTest");
        EntityModel<Customer> savedCustomer = customerService.saveCustomer(newCustomer);

        Assertions.assertEquals(1, Objects.requireNonNull(savedCustomer.getContent()).getId());
        Assertions.assertEquals("FirstNameTest", savedCustomer.getContent().getFirstName());
        Assertions.assertEquals("LastNameTest", savedCustomer.getContent().getLastName());
    }

    @Test
    @Order(2)
    void haveToGetAllCustomers() {
        customerService.saveCustomer(new Customer("FirstNameTest", "LastNameTest"));
        customerService.saveCustomer(new Customer("FirstNameTest2", "LastNameTest2"));
        customerService.saveCustomer(new Customer("FirstNameTest3", "LastNameTest3"));

        Assertions.assertEquals(3, customerService.getCustomers().size());
    }

    @Test
    @Order(3)
    void haveToGetCustomerById() {
        EntityModel<Customer> savedCustomer = customerService.saveCustomer(new Customer("FirstName", "LastName"));
        EntityModel<Customer> currentCustomer = customerService.getCustomerById(Objects.requireNonNull(savedCustomer.getContent()).getId());

        Assertions.assertEquals(savedCustomer.getContent().getId(), Objects.requireNonNull(currentCustomer.getContent()).getId());
        Assertions.assertEquals("FirstName", currentCustomer.getContent().getFirstName());
        Assertions.assertEquals("LastName", currentCustomer.getContent().getLastName());
    }

    @Test
    @Order(4)
    void haveToGetCustomersByLastname() {
        customerService.saveCustomer(new Customer("FirstNameTest", "LastNameTest"));
        customerService.saveCustomer(new Customer("FirstNameTest2", "LastNameTest"));
        customerService.saveCustomer(new Customer("FirstNameTest3", "LastNameTest"));

        List<EntityModel<Customer>> customerList = customerService.getCustomersByLastName("LastNameTest");

        Assertions.assertEquals(3, customerList.size());
    }

    @Test
    @Order(5)
    void haveToDeleteACustomer() {
        EntityModel<Customer> savedCustomer = customerService.saveCustomer(new Customer("FirstNameTest", "LastNameTest"));
        customerService.deleteCustomerById(Objects.requireNonNull(savedCustomer.getContent()).getId());

        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(savedCustomer.getContent().getId()));
    }

    @Test
    @Order(6)
    void haveToSaveAnEmployee() {
        Employee employee = new Employee("Filipe", "Tortora", "Programador");

        EntityModel<Employee> response = employeeService.saveEmployee(employee);

        Assertions.assertEquals("Filipe", Objects.requireNonNull(response.getContent()).getFirstName());
        Assertions.assertEquals("Tortora", response.getContent().getLastName());
        Assertions.assertEquals("Programador", response.getContent().getRole());
    }

    @Test
    @Order(7)
    void haveToGetAllEmployees() {
        employeeService.saveEmployee(new Employee("Filipe", "Tortora", "Programador"));
        employeeService.saveEmployee(new Employee("Debora", "Brandao", "Contadora"));
        employeeService.saveEmployee(new Employee("Best", "Sauro", "Cabeleileira"));

        List<EntityModel<Employee>> employees = employeeService.getEmployees();

        Assertions.assertEquals(3, employees.size());
    }

    @Test
    @Order(8)
    void haveToUpdateAnEmployee() {
        Employee employee = new Employee("Filipe", "Tortora", "Programador");
        employeeService.saveEmployee(employee);

        Employee newEmployee = new Employee("Felipe", "Dias", "Analista");
        EntityModel<Employee> updatedEmployee = employeeService.updateEmployee(newEmployee, 1L);

        Assertions.assertEquals("Felipe", Objects.requireNonNull(updatedEmployee.getContent()).getFirstName());
        Assertions.assertEquals("Dias", updatedEmployee.getContent().getLastName());
        Assertions.assertEquals("Analista", updatedEmployee.getContent().getRole());
    }

    @Test
    @Order(9)
    void haveToDeleteAnEmployee() {
        Employee employee = new Employee("Filipe", "Tortora", "Programador");
        EntityModel<Employee> response = employeeService.saveEmployee(employee);
        employeeService.deleteEmployeeById(Objects.requireNonNull(response.getContent()).getId());

        Assertions.assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(1L));
    }

    @Test
    @Order(10)
    void haveToSaveOrder() {
        EntityModel<com.tortora.financas.model.Order> savedOrder = orderService.saveOrder(new com.tortora.financas.model.Order("Ordem 1", Status.IN_PROGRESS));

        Assertions.assertEquals(1, Objects.requireNonNull(savedOrder.getContent()).getId());
        Assertions.assertEquals("Ordem 1", savedOrder.getContent().getDescription());
        Assertions.assertEquals(Status.IN_PROGRESS, savedOrder.getContent().getStatus());
    }

    @Test
    @Order(11)
    void haveToGetAllOrders() {
        orderService.saveOrder(new com.tortora.financas.model.Order("Ordem 1", Status.IN_PROGRESS));
        orderService.saveOrder(new com.tortora.financas.model.Order("Ordem 2", Status.IN_PROGRESS));
        orderService.saveOrder(new com.tortora.financas.model.Order("Ordem 3", Status.IN_PROGRESS));
        List<EntityModel<com.tortora.financas.model.Order>> orderList = orderService.getOrders();

        Assertions.assertEquals(3, orderList.size());
    }

    @Test
    @Order(12)
    void haveToGetOrderById() {
        EntityModel<com.tortora.financas.model.Order> savedOrder = orderService.saveOrder(new com.tortora.financas.model.Order("Ordem 1", Status.IN_PROGRESS));
        com.tortora.financas.model.Order order = orderService.getOrderById(Objects.requireNonNull(savedOrder.getContent()).getId());

        Assertions.assertEquals(savedOrder.getContent().getId(), order.getId());
        Assertions.assertEquals("Ordem 1", order.getDescription());
        Assertions.assertEquals(Status.IN_PROGRESS, order.getStatus());
    }

    @Test
    @Order(13)
    void haveToGetEntityModelOrderById() {
        EntityModel<com.tortora.financas.model.Order> savedOrder = orderService.saveOrder(new com.tortora.financas.model.Order("Ordem 1", Status.IN_PROGRESS));
        EntityModel<com.tortora.financas.model.Order> order = orderService.getEntityModelOrderById(Objects.requireNonNull(savedOrder.getContent()).getId());

        Assertions.assertEquals(savedOrder.getContent().getId(), Objects.requireNonNull(order.getContent()).getId());
        Assertions.assertEquals("Ordem 1", order.getContent().getDescription());
        Assertions.assertEquals(Status.IN_PROGRESS, order.getContent().getStatus());
    }

    @Test
    @Order(14)
    void haveToCancelAnOrder() {
        EntityModel<com.tortora.financas.model.Order> savedOrder = orderService.saveOrder(new com.tortora.financas.model.Order("Ordem 1", Status.IN_PROGRESS));
        orderService.cancelOrder(Objects.requireNonNull(savedOrder.getContent()));
        com.tortora.financas.model.Order currentOrder = orderService.getOrderById(savedOrder.getContent().getId());

        Assertions.assertEquals(Status.CANCELLED, currentOrder.getStatus());
    }

    @Test
    @Order(15)
    void haveToCompleteAnOrder() {
        EntityModel<com.tortora.financas.model.Order> savedOrder = orderService.saveOrder(new com.tortora.financas.model.Order("Ordem 1", Status.IN_PROGRESS));
        orderService.completeOrder(Objects.requireNonNull(savedOrder.getContent()));
        com.tortora.financas.model.Order currentOrder = orderService.getOrderById(savedOrder.getContent().getId());

        Assertions.assertEquals(Status.COMPLETED, currentOrder.getStatus());
    }

    @Test
    @Order(16)
    void haveToGetAllOrdersInProgress() {
        orderService.saveOrder(new com.tortora.financas.model.Order("Ordem 1", Status.IN_PROGRESS));
        orderService.saveOrder(new com.tortora.financas.model.Order("Ordem 2", Status.IN_PROGRESS));
        // O saveOrder sempre altera o status para IN_PROGRESS então serão 3 itens na lista
        orderService.saveOrder(new com.tortora.financas.model.Order("Ordem 3", Status.CANCELLED));
        List<EntityModel<com.tortora.financas.model.Order>> orderList = orderService.getOrdersByStatus(Status.IN_PROGRESS);

        Assertions.assertEquals(3, orderList.size());
    }

    @Test
    @Order(17)
    void haveToSaveUser() {
        int afterSave = ((Collection<?>) userService.getUsers()).size();
        userService.saveUser("Filipe", "fedispato@gmail.com");
        int beforeSave = ((Collection<?>) userService.getUsers()).size();

        Assertions.assertEquals(1, beforeSave - afterSave);
    }

    @Test
    @Order(18)
    void haveToGetAllUsers() {
        userService.saveUser("Filipe", "fedispato@gmail.com");
        userService.saveUser("Debora", "debora@gmail.com");
        userService.saveUser("Best", "best@gmail.com");
        int numberOfUsers = ((Collection<?>) userService.getUsers()).size();

        Assertions.assertEquals(3, numberOfUsers);
    }

    @Test
    @Order(19)
    void haveToDeleteAllUsers() {
        userService.saveUser("Filipe", "fedispato@gmail.com");
        userService.saveUser("Debora", "debora@gmail.com");
        userService.saveUser("Best", "best@gmail.com");
        userService.deleteUsers();

        int numberOfUsers = ((Collection<?>) userService.getUsers()).size();

        Assertions.assertEquals(0, numberOfUsers);
    }

    @Test
    @Order(20)
    void haveToMigrateEmployees() {
        List<EntityModel<Employee>> response = employeeService.migrateEmployees("https://65ae79a41dfbae409a74ebbc.mockapi.io/api/v1/employee");
        Assertions.assertEquals(5, response.size());
    }

}
