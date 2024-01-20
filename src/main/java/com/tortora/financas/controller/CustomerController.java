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

    private final CustomerService service;

    CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping("/customers/{id}")
    public EntityModel<Customer> oneCustomer(@PathVariable Long id) {
        return service.getCustomerById(id);
    }

    @GetMapping("/customers")
    public CollectionModel<EntityModel<Customer>> allCustomers() {
        List<EntityModel<Customer>> customers = service.getCustomers();
        return CollectionModel.of(customers, linkTo(methodOn(CustomerController.class)
                .allCustomers())
                .withSelfRel());
    }

    @GetMapping("/customersByLastName/{lastName}")
    public CollectionModel<EntityModel<Customer>> customersByLastName(@PathVariable String lastName) {
        List<EntityModel<Customer>> customers = service.getCustomersByLastName(lastName);
        return CollectionModel.of(customers, linkTo(methodOn(CustomerController.class)
                .customersByLastName(lastName))
                .withSelfRel());
    }

    @PostMapping("/customers")
    ResponseEntity<?> newCustomer(@RequestBody Customer newCustomer) {
        EntityModel<Customer> entityModel = service.saveCustomer(newCustomer);
        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

}
