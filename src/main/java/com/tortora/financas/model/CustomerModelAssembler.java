package com.tortora.financas.model;

import com.tortora.financas.controller.CustomerController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CustomerModelAssembler implements RepresentationModelAssembler<Customer, EntityModel<Customer>> {

    @Override
    public EntityModel<Customer> toModel(Customer customer) {

        return EntityModel.of(customer, //
                linkTo(methodOn(CustomerController.class).oneCustomerById(customer.getId())).withSelfRel(),
                linkTo(methodOn(CustomerController.class).customerByLastName(customer.getLastName())).withSelfRel(),
                linkTo(methodOn(CustomerController.class).allCustomers()).withRel("customers"));
    }

}
