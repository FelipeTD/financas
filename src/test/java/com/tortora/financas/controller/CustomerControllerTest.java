package com.tortora.financas.controller;

import com.tortora.financas.enums.Status;
import com.tortora.financas.model.*;
import com.tortora.financas.service.CustomerService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService service;

    @Spy
    private CustomerModelAssembler assembler;

    @Test
    void allCustomersTest() throws Exception {

        Customer c = new Customer("Filipe", "Tortora");
        Customer c2 = new Customer("Debora", "Brandao");

        List<Customer> customerList = new ArrayList<>();
        customerList.add(c);
        customerList.add(c2);

        List<EntityModel<Customer>> list = customerList.stream()
                .map(assembler::toModel).toList();

        when(service.getCustomers()).thenReturn(list);
        this.mockMvc.perform(get("/customers")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.customerList").exists())
                .andExpect(jsonPath("$._embedded.customerList[*].firstName").exists());
    }

    @Test
    void oneCustomerTest() throws Exception {

        Customer c = new Customer("Filipe", "Tortora");
        EntityModel<Customer> customerEntityModel = assembler.toModel(c);

        when(service.getCustomerById(1L)).thenReturn(customerEntityModel);
        this.mockMvc.perform(get("/customers/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Filipe"));
    }

    @Test
    void customersByLastNameTest() throws Exception {

        Customer c = new Customer("Filipe", "Tortora");
        List<Customer> customerList = new ArrayList<>();
        customerList.add(c);

        List<EntityModel<Customer>> list = customerList.stream()
                .map(assembler::toModel).toList();

        when(service.getCustomersByLastName("Tortora")).thenReturn(list);
        this.mockMvc.perform(get("/customersByLastName/Tortora")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.customerList").exists())
                .andExpect(jsonPath("$._embedded.customerList[*].firstName").value("Filipe"));
    }

    @Test
    void newCustomerTest() throws Exception {
        Customer c = new Customer("Filipe", "Tortora");
        EntityModel<Customer> customerEntityModel = assembler.toModel(c);

        when(service.saveCustomer(c)).thenReturn(customerEntityModel);
        this.mockMvc.perform(post("/customers")
                        .content(asJsonString(c))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").exists());
    }

    @Test
    void customerToStringTest() {
        Customer c = new Customer("Filipe", "Tortora");
        Assertions.assertEquals("Customer{id=null, firstName='Filipe', lastName='Tortora'}", c.toString());
    }

    @Test
    void customerHashCodeTest() {
        Customer c = new Customer("Filipe", "Tortora");
        Assertions.assertEquals(1337399051, c.hashCode());
    }

    @Test
    void customerEqualsTest() {

        // Objetos iguais
        Customer c = new Customer("Filipe", "Tortora");
        c.setId(1L);
        Customer c2 = new Customer("Filipe", "Tortora");
        c2.setId(1L);

        // ID diferente
        Customer c3 = new Customer("Felipe", "Tortora");
        c3.setId(2L);

        // firstName diferente
        Customer c4 = new Customer("Felipe", "Tortora");
        c4.setId(1L);

        // lastName diferente
        Customer c5 = new Customer("Filipe", "Dias");
        c5.setId(1L);

        // Objeto diferente
        Order order = new Order("minha ordem", Status.IN_PROGRESS);

        Assertions.assertEquals(c, c);
        Assertions.assertNotEquals(c, order);
        Assertions.assertEquals(c, c2);
        Assertions.assertNotEquals(c, c3);
        Assertions.assertNotEquals(c, c4);
        Assertions.assertNotEquals(c, c5);
    }

}
