package com.tortora.financas.service;

import com.tortora.financas.enums.Status;
import com.tortora.financas.exceptions.OrderNotFoundException;
import com.tortora.financas.model.Order;
import com.tortora.financas.model.OrderModelAssembler;
import com.tortora.financas.repository.OrderRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderModelAssembler assembler;

    public OrderService(OrderRepository orderRepository, OrderModelAssembler assembler) {
        this.orderRepository = orderRepository;
        this.assembler = assembler;
    }

    public List<EntityModel<Order>> getOrders() {
        return orderRepository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());
    }

    public EntityModel<Order> getOrderById(Long id) {
        Order order = orderRepository.findById(id) //
                .orElseThrow(() -> new OrderNotFoundException(id));

        return assembler.toModel(order);
    }

    public EntityModel<Order> saveOrder(Order order) {
        order.setStatus(Status.IN_PROGRESS);
        return assembler.toModel(orderRepository.save(order));
    }

    public EntityModel<Order> assemblyOrder(Order order) {
        return assembler.toModel(order);
    }

    public EntityModel<Order> cancelOrder(Order order) {
        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.CANCELLED);
            return assembler.toModel(orderRepository.save(order));
        }

        return null;
    }

    public EntityModel<Order> completeOrder(Order order) {
        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.COMPLETED);
            return assembler.toModel(orderRepository.save(order));
        }

        return null;


    }

}
