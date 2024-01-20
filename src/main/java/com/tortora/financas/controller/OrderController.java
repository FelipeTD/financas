package com.tortora.financas.controller;

import com.tortora.financas.model.Order;
import com.tortora.financas.service.OrderService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderController {

    private final OrderService service;

    OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping("/orders")
    public CollectionModel<EntityModel<Order>> allOrders() {
        List<EntityModel<Order>> orders = service.getOrders();
        return CollectionModel.of(orders, //
                linkTo(methodOn(OrderController.class).allOrders()).withSelfRel());
    }

    @GetMapping("/orders/{id}")
    public EntityModel<Order> oneOrder(@PathVariable Long id) {
        return service.getOrderById(id);
    }

    @PostMapping("/orders")
    ResponseEntity<EntityModel<Order>> newOrder(@RequestBody Order order) {
        EntityModel<Order> newOrder = service.saveOrder(order);

        return ResponseEntity //
                .created(linkTo(methodOn(OrderController.class).oneOrder(Objects.requireNonNull(newOrder.getContent()).getId())).toUri()) //
                .body(newOrder);
    }

    @DeleteMapping("/orders/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        Order order = service.getOrderById(id).getContent();
        assert order != null;

        EntityModel<Order> response = service.cancelOrder(order);

        return response != null ? ResponseEntity.ok(response) : ResponseEntity //
                .status(HttpStatus.METHOD_NOT_ALLOWED) //
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
                .body(Problem.create() //
                        .withTitle("Method not allowed") //
                        .withDetail("You can't cancel an order that is in the " + order.getStatus() + " status"));
    }

    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable Long id) {
        Order order = service.getOrderById(id).getContent();
        assert order != null;

        EntityModel<Order> response = service.completeOrder(order);

        return response != null ? ResponseEntity.ok(response) : ResponseEntity //
                .status(HttpStatus.METHOD_NOT_ALLOWED) //
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
                .body(Problem.create() //
                        .withTitle("Method not allowed") //
                        .withDetail("You can't complete an order that is in the " + order.getStatus() + " status"));
    }

}