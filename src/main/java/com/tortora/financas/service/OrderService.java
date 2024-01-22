package com.tortora.financas.service;

import com.tortora.financas.enums.Status;
import com.tortora.financas.exceptions.OrderNotFoundException;
import com.tortora.financas.model.Order;
import com.tortora.financas.model.OrderModelAssembler;
import com.tortora.financas.repository.OrderRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderModelAssembler orderModelAssembler;

    public OrderService(OrderRepository orderRepository, OrderModelAssembler orderModelAssembler) {
        this.orderRepository = orderRepository;
        this.orderModelAssembler = orderModelAssembler;
    }

    public EntityModel<Order> saveOrder(Order order) {
        order.setStatus(Status.IN_PROGRESS);
        return orderModelAssembler.toModel(orderRepository.save(order));
    }

    public List<EntityModel<Order>> getOrders() {
        return orderRepository.findAll().stream() //
                .map(orderModelAssembler::toModel) //
                .collect(Collectors.toList());
    }

    public Order getOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent()) {
            return order.get();
        } else {
            throw new OrderNotFoundException(orderId);
        }
    }

    public EntityModel<Order> getEntityModelOrderById(Long orderId) {
        return orderModelAssembler.toModel(getOrderById(orderId));
    }

    public EntityModel<Order> cancelOrder(Order order) {
        return executeOrder(order, Status.CANCELLED);
    }

    public EntityModel<Order> completeOrder(Order order) {
        return executeOrder(order, Status.COMPLETED);
    }

    public List<EntityModel<Order>> getOrdersByStatus(Status status) {
        return orderRepository.findOrderByStatus(status).stream() //
                .map(orderModelAssembler::toModel) //
                .collect(Collectors.toList());
    }

    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }

    private EntityModel<Order> executeOrder(Order order, Status status) {
        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(status);
            return orderModelAssembler.toModel(orderRepository.save(order));
        }
        return null;
    }

}
