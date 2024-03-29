package com.tortora.financas.service;

import com.tortora.financas.exceptions.EmployeeNotFoundException;
import com.tortora.financas.model.Employee;
import com.tortora.financas.model.request.MigrateEmployeeRequest;
import com.tortora.financas.repository.EmployeeRepository;
import com.tortora.financas.utils.ConverterUtils;
import com.tortora.financas.utils.HttpUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EmployeeServiceTest {

    @MockBean
    private EmployeeRepository repository;

    @MockBean
    private HttpUtils httpUtils;

    @MockBean
    private ConverterUtils converterUtils;

    @Autowired
    private EmployeeService service;

    @Test
    void getEmployeesTest() {
        Employee e = new Employee("Filipe", "Tortora", "Programador");
        Employee e2 = new Employee("Debora", "Brandao", "Contadora");
        List<Employee> list = new ArrayList<>();
        list.add(e);
        list.add(e2);

        when(repository.findAll()).thenReturn(list);
        List<EntityModel<Employee>> response = service.getEmployees();
        Assertions.assertEquals(2, response.size());
    }

    @Test
    void saveEmployeeTest() {
        Employee e = new Employee("Filipe", "Tortora", "Programador");

        when(repository.save(e)).thenReturn(e);
        EntityModel<Employee> response = service.saveEmployee(e);
        Assertions.assertEquals("Filipe", Objects.requireNonNull(response.getContent()).getFirstName());
    }

    @Test
    void migrateEmployeesJSONTest() {
        Employee e = new Employee("Filipe", "Tortora", "Programador");
        HttpURLConnection mockHttpURLConnection = mock(HttpURLConnection.class);
        String output = "[{\"firstName\":\"firstName 1\",\"lastName\":\"lastName 1\",\"role\":\"role 1\",\"id\":\"1\"}]";
        String url = "http://teste.com.br";
        MigrateEmployeeRequest request = new MigrateEmployeeRequest(url, "JSON");

        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("Filipe", "Tortora", "Programador"));

        when(converterUtils.convertJsonEmployees(url)).thenReturn(employees);
        // when(httpUtils.get(url)).thenReturn(mockHttpURLConnection);
        // when(httpUtils.reader(mockHttpURLConnection)).thenReturn(new StringBuilder(output));
        when(repository.save(e)).thenReturn(e);

        List<EntityModel<Employee>> response = service.migrateEmployees(request);
        Assertions.assertEquals(1, response.size());
    }

    @Test
    void migrateEmployeesXMLTest() {
        Employee e = new Employee("Filipe", "Tortora", "Programador");
        String url = "http://teste.com.br";
        MigrateEmployeeRequest request = new MigrateEmployeeRequest(url, "XML");

        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("Filipe", "Tortora", "Programador"));

        when(converterUtils.convertXMLEmployees(url)).thenReturn(employees);
        when(repository.save(e)).thenReturn(e);

        List<EntityModel<Employee>> response = service.migrateEmployees(request);
        Assertions.assertEquals(1, response.size());
    }

    @Test
    void getEmployeeByIdTest() {
        Optional<Employee> e = Optional.of(new Employee("Filipe", "Tortora", "Programador"));

        when(repository.findById(1L)).thenReturn(e);
        EntityModel<Employee> response = service.getEmployeeById(1L);
        Assertions.assertEquals("Filipe", Objects.requireNonNull(response.getContent()).getFirstName());
    }

    @Test
    void getEmployeeByIdEmployeeNotFoundTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        try {
            service.getEmployeeById(1L);
        } catch (EmployeeNotFoundException e) {
            Assertions.assertEquals("Could not find employee 1", e.getMessage());
        }
    }

    @Test
    void updateEmployeeTest() {
        Employee e = new Employee("Filipe", "Tortora", "Programador");
        e.setId(1L);
        Optional<Employee> optionalEmployee = Optional.of(e);

        Employee newEmployee = new Employee("Debora", "Brandao", "Contadora");

        when(repository.findById(1L)).thenReturn(optionalEmployee);
        when(repository.save(newEmployee)).thenReturn(newEmployee);

        EntityModel<Employee> response = service.updateEmployee(newEmployee, 1L);
        Assertions.assertEquals("Debora", Objects.requireNonNull(response.getContent()).getFirstName());
    }

    @Test
    void deleteEmployeeByIdTest() {
        Optional<Employee> e = Optional.of(new Employee("Filipe", "Tortora", "Programador"));

        when(repository.findById(1L)).thenReturn(e);

        try {
            service.deleteEmployeeById(1L);
        } catch (EmployeeNotFoundException exception) {
            Assertions.assertEquals("Could not find employee 1", exception.getMessage());
        }
    }

    @Test
    void deleteEmployeeByIdEmployeeNotFoundTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        try {
            service.deleteEmployeeById(1L);
        } catch (EmployeeNotFoundException exception) {
            Assertions.assertEquals("Could not find employee 1", exception.getMessage());
        }
    }

}
