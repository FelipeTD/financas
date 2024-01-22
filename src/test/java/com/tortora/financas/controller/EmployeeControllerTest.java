package com.tortora.financas.controller;

import com.tortora.financas.enums.Status;
import com.tortora.financas.exceptions.EmployeeNotFoundException;
import com.tortora.financas.model.Employee;
import com.tortora.financas.model.EmployeeModelAssembler;
import com.tortora.financas.model.Order;
import com.tortora.financas.service.EmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.tortora.financas.utils.TestUtils.asJsonString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService service;
    @Spy
    private EmployeeModelAssembler assembler;

    @Test
    void allEmployeesTest() throws Exception {

        Employee e = new Employee("Filipe", "Dias", "Programador");
        Employee e2 = new Employee("Debora", null, "Contadora");

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(e);
        employeeList.add(e2);

        List<EntityModel<Employee>> list = employeeList.stream()
                .map(assembler::toModel).toList();

        when(service.getEmployees()).thenReturn(list);
        this.mockMvc.perform(get("/employees")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.employeeList").exists())
                .andExpect(jsonPath("$._embedded.employeeList[*].firstName").exists());

    }

    @Test
    void oneEmployeeTest() throws Exception {
        Employee e = new Employee("Filipe", "Dias", "Programador");
        EntityModel<Employee> employeeEntityModel = assembler.toModel(e);

        when(service.getEmployeeById(1L)).thenReturn(employeeEntityModel);
        this.mockMvc.perform(get("/employees/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Filipe"));
    }

    @Test
    void oneEmployeeNotFoundTest() throws Exception {

        when(service.getEmployeeById(1L)).thenThrow(EmployeeNotFoundException.class);
        this.mockMvc.perform(get("/employees/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void newEmployeeTest() throws Exception {
        Employee e = new Employee("Filipe", "Dias", "Programador");
        EntityModel<Employee> employeeEntityModel = assembler.toModel(e);

        when(service.saveEmployee(e)).thenReturn(employeeEntityModel);
        this.mockMvc.perform(post("/employees")
                        .content(asJsonString(e))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").exists());
    }

    @Test
    void migrateEmployeesTest() throws Exception {
        Employee e = new Employee("Filipe", "Dias", "Programador");
        Employee e2 = new Employee("Filipe2", "Dias2", "Programador2");
        Employee e3 = new Employee("Filipe3", "Dias3", "Programador3");

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(e);
        employeeList.add(e2);
        employeeList.add(e3);

        List<EntityModel<Employee>> list = employeeList.stream()
                .map(assembler::toModel).toList();

        when(service.migrateEmployees("http://teste.com.br")).thenReturn(list);
        this.mockMvc.perform(post("/employees/migrate")
                        .param("url", "http://teste.com.br")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.employeeList").exists())
                .andExpect(jsonPath("$._embedded.employeeList[*].firstName").exists());
    }

    @Test
    void replaceEmployeeTest() throws Exception {
        Employee e = new Employee("Filipe", "Dias", "Programador");
        EntityModel<Employee> employeeEntityModel = assembler.toModel(e);

        when(service.updateEmployee(e, 1L)).thenReturn(employeeEntityModel);
        this.mockMvc.perform(put("/employees/{id}", 1L)
                .content(asJsonString(e))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Filipe"))
                .andExpect(jsonPath("$.lastName").value("Dias"))
                .andExpect(jsonPath("$.role").value("Programador"));

    }

    @Test
    void deleteEmployeeTest() throws Exception {
        this.mockMvc.perform(delete("/employees/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void employeeToStringTest() {
        Employee e = new Employee("Filipe", "Tortora", "Programador");
        Assertions.assertEquals("Employee{id=null, firstName='Filipe', lastName='Tortora', role='Programador'}", e.toString());
    }

    @Test
    void employeeHashCodeTest() {
        Employee e = new Employee("Filipe", "Tortora", "Programador");
        Assertions.assertEquals(1188514815, e.hashCode());
    }

    @Test
    void employeeEqualsTest() {

        // Objetos iguais
        Employee e = new Employee("Filipe", "Tortora", "Programador");
        e.setId(1L);
        Employee e2 = new Employee("Filipe", "Tortora", "Programador");
        e2.setId(1L);

        // ID diferente
        Employee e3 = new Employee("Filipe", "Tortora", "Programador");
        e3.setId(2L);

        // firstName diferente
        Employee e4 = new Employee("Felipe", "Tortora", "Programador");
        e4.setId(1L);

        // lastName diferente
        Employee e5 = new Employee("Filipe", "Dias", "Programador");
        e5.setId(1L);

        // role diferente
        Employee e6 = new Employee("Filipe", "Tortora", "Analista");
        e6.setId(1L);

        // Objeto diferente
        Order order = new Order("minha ordem", Status.IN_PROGRESS);

        Assertions.assertEquals(e, e);
        Assertions.assertNotEquals(e, order);
        Assertions.assertEquals(e, e2);
        Assertions.assertNotEquals(e, e3);
        Assertions.assertNotEquals(e, e4);
        Assertions.assertNotEquals(e, e5);
        Assertions.assertNotEquals(e, e6);
    }

}
