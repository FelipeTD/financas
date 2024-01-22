package com.tortora.financas.controller;

import com.tortora.financas.model.Customer;
import com.tortora.financas.service.CustomerService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers/{customerId}")
    public EntityModel<Customer> oneCustomerById(@PathVariable Long customerId) {
        return customerService.getCustomerById(customerId);
    }

    @GetMapping("/customers")
    public CollectionModel<EntityModel<Customer>> allCustomers() {
        List<EntityModel<Customer>> customers = customerService.getCustomers();
        return CollectionModel.of(customers, linkTo(methodOn(CustomerController.class)
                .allCustomers())
                .withSelfRel());
    }

    @GetMapping("/customersByLastName/{customerLastName}")
    public CollectionModel<EntityModel<Customer>> customerByLastName(@PathVariable String customerLastName) {
        List<EntityModel<Customer>> customers = customerService.getCustomersByLastName(customerLastName);
        return CollectionModel.of(customers, linkTo(methodOn(CustomerController.class)
                .customerByLastName(customerLastName))
                .withSelfRel());
    }

    @PostMapping("/customers")
    ResponseEntity<?> addNewUser(@RequestBody Customer newCustomer) {
        EntityModel<Customer> entityModel = customerService.saveCustomer(newCustomer);
        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @DeleteMapping("/customers/{customerId}")
    ResponseEntity<?> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.noContent().build();
    }

}
