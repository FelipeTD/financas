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

    private final CustomerRepository customerRepository;

    private final CustomerModelAssembler customerModelAssembler;

    public CustomerService(CustomerRepository customerRepository, CustomerModelAssembler customerModelAssembler) {
        this.customerRepository = customerRepository;
        this.customerModelAssembler = customerModelAssembler;
    }

    public List<EntityModel<Customer>> getCustomers() {
        return customerRepository.findAll().stream()
                .map(customerModelAssembler::toModel)
                .collect(Collectors.toList());
    }

    public EntityModel<Customer> getCustomerById(Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isPresent()) {
            return customerModelAssembler.toModel(customer.get());
        } else {
            throw new CustomerNotFoundException(customerId);
        }
    }

    public List<EntityModel<Customer>> getCustomersByLastName(String customerLastName) {
        return customerRepository.findByLastName(customerLastName).stream()
                .map(customerModelAssembler::toModel)
                .collect(Collectors.toList());
    }

    public EntityModel<Customer> saveCustomer(Customer newCustomer) {
        return customerModelAssembler.toModel(customerRepository.save(newCustomer));
    }

    public void deleteCustomerById(Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isPresent()) {
            customerRepository.deleteById(customerId);
        } else {
            throw new CustomerNotFoundException(customerId);
        }
    }

    public void deleteAllCustomers() {
        customerRepository.deleteAll();
    }

}
