package com.tortora.financas.service;

import com.tortora.financas.exceptions.CustomerNotFoundException;
import com.tortora.financas.model.Customer;
import com.tortora.financas.model.CustomerModelAssembler;
import com.tortora.financas.repository.CustomerRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    private final CustomerModelAssembler assembler;

    public CustomerService(CustomerRepository repository, CustomerModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    public List<EntityModel<Customer>> getCustomers() {
        return repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }

    public EntityModel<Customer> getCustomerById(Long id) {
        Optional<Customer> customer = repository.findById(id);

        if (customer.isPresent()) {
            return assembler.toModel(customer.get());
        } else {
            throw new CustomerNotFoundException(id);
        }
    }

    public List<EntityModel<Customer>> getCustomersByLastName(String lastName) {
        return repository.findByLastName(lastName).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }

    public EntityModel<Customer> saveCustomer(Customer newCustomer) {
        return assembler.toModel(repository.save(newCustomer));
    }

}
