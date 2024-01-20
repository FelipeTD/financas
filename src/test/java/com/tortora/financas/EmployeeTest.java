package com.tortora.financas;

import com.tortora.financas.controller.EmployeeController;
import com.tortora.financas.model.Employee;
import com.tortora.financas.model.EmployeeModelAssembler;
import com.tortora.financas.service.EmployeeService;
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
public class EmployeeTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService service;
    @Spy
    private EmployeeModelAssembler assembler;

    @Test
    void allEmployeesTest() throws Exception {

        Employee e = new Employee("Filipe", "Dias", "Programador");
        Employee e2 = new Employee("Debora", "Brandao", "Contadora");

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

}
