package com.tortora.financas.services;

import com.tortora.financas.exceptions.CustomerNotFoundException;
import com.tortora.financas.model.Customer;
import com.tortora.financas.repository.CustomerRepository;
import com.tortora.financas.service.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomerServiceTest {

    @MockBean
    private CustomerRepository repository;

    @Autowired
    private CustomerService service;

    @Test
    void getCustomersTest() {
        Customer c = new Customer("Filipe", "Tortora");
        Customer c2 = new Customer("Debora", "Brandao");

        List<Customer> list = new ArrayList<>();
        list.add(c);
        list.add(c2);

        when(repository.findAll()).thenReturn(list);
        List<EntityModel<Customer>> response = service.getCustomers();
        Assertions.assertEquals(2, response.size());
    }

    @Test
    void getCustomerByIdTest() {
        Optional<Customer> c = Optional.of(new Customer("Filipe", "Tortora"));
        when(repository.findById(1L)).thenReturn(c);
        EntityModel<Customer> response = service.getCustomerById(1L);
        Assertions.assertEquals("Filipe", Objects.requireNonNull(response.getContent()).getFirstName());
    }

    @Test
    void getCustomersByLastNameTest() {
        Customer c = new Customer("Filipe", "Tortora");
        List<Customer> list = new ArrayList<>();
        list.add(c);

        when(repository.findByLastName("Tortora")).thenReturn(list);
        List<EntityModel<Customer>> response = service.getCustomersByLastName("Tortora");
        Assertions.assertEquals(1, response.size());
    }

    @Test
    void saveCustomerTest() {
        Customer c = new Customer("Filipe", "Tortora");

        when(repository.save(c)).thenReturn(c);
        EntityModel<Customer> response = service.saveCustomer(c);
        Assertions.assertEquals("Filipe", Objects.requireNonNull(response.getContent()).getFirstName());
    }

    @Test
    void getCustomerByIdCustomerNotFoundTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        try {
            service.getCustomerById(1L);
        } catch (CustomerNotFoundException e) {
            Assertions.assertEquals("Could not find customer 1", e.getMessage());
        }
    }

}
