package com.tortora.financas.controller;

import com.tortora.financas.enums.Status;
import com.tortora.financas.exceptions.OrderNotFoundException;
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

    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<CollectionModel<EntityModel<Order>>> allOrders() {
        List<EntityModel<Order>> orders = orderService.getOrders();
        return ResponseEntity.ok(CollectionModel.of(orders, //
                linkTo(methodOn(OrderController.class).allOrders()).withSelfRel()));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> oneOrderById(@PathVariable Long orderId) {
        try {
            EntityModel<Order> order = orderService.getEntityModelOrderById(orderId);
            return ResponseEntity.ok(order);
        } catch (OrderNotFoundException e) {
            return ResponseEntity //
                    .status(HttpStatus.NOT_FOUND) //
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
                    .body(Problem.create() //
                            .withTitle("Order Not Found") //
                            .withDetail(e.getMessage()));
        }
    }

    @GetMapping("/orders/status/{status}")
    public ResponseEntity<CollectionModel<EntityModel<Order>>> ordersByStatus(@PathVariable Status status) {
        List<EntityModel<Order>> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).allOrders()).withSelfRel()));
    }

    @PostMapping("/orders")
    ResponseEntity<EntityModel<Order>> addNewOrder(@RequestBody Order order) {
        EntityModel<Order> newOrder = orderService.saveOrder(order);

        return ResponseEntity //
                .created(linkTo(methodOn(OrderController.class).oneOrderById(Objects.requireNonNull(newOrder.getContent()).getId())).toUri()) //
                .body(newOrder);
    }

    @DeleteMapping("/orders/{orderId}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);

        EntityModel<Order> orderResponse = orderService.cancelOrder(order);

        return orderResponse != null ? ResponseEntity.ok(orderResponse) : ResponseEntity //
                .status(HttpStatus.METHOD_NOT_ALLOWED) //
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
                .body(Problem.create() //
                        .withTitle("Method not allowed") //
                        .withDetail("You can't cancel an order that is in the " + order.getStatus() + " status"));
    }

    @PutMapping("/orders/{orderId}/complete")
    public ResponseEntity<?> complete(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);

        EntityModel<Order> orderResponse = orderService.completeOrder(order);

        return orderResponse != null ? ResponseEntity.ok(orderResponse) : ResponseEntity //
                .status(HttpStatus.METHOD_NOT_ALLOWED) //
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
                .body(Problem.create() //
                        .withTitle("Method not allowed") //
                        .withDetail("You can't complete an order that is in the " + order.getStatus() + " status"));
    }

}