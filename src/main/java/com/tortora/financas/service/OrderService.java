package com.tortora.financas.service;

import com.tortora.financas.enums.Status;
import com.tortora.financas.exceptions.EmployeeNotFoundException;
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

    private final OrderRepository repository;
    private final OrderModelAssembler assembler;

    public OrderService(OrderRepository orderRepository, OrderModelAssembler assembler) {
        this.repository = orderRepository;
        this.assembler = assembler;
    }

    public EntityModel<Order> saveOrder(Order order) {
        order.setStatus(Status.IN_PROGRESS);
        return assembler.toModel(repository.save(order));
    }

    public List<EntityModel<Order>> getOrders() {
        return repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());
    }

    public Order getOrderById(Long id) {
        Optional<Order> order = repository.findById(id);

        if (order.isPresent()) {
            return order.get();
        } else {
            throw new OrderNotFoundException(id);
        }
    }

    public EntityModel<Order> getEntityModelOrderById(Long id) {
        return assembler.toModel(getOrderById(id));
    }

    public EntityModel<Order> cancelOrder(Order order) {
        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.CANCELLED);
            return assembler.toModel(repository.save(order));
        }

        return null;
    }

    public EntityModel<Order> completeOrder(Order order) {
        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.COMPLETED);
            return assembler.toModel(repository.save(order));
        }

        return null;
    }

    public void deleteAllOrders() {
        repository.deleteAll();
    }

}
