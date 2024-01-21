package com.tortora.financas.service;

import com.tortora.financas.enums.Status;
import com.tortora.financas.exceptions.OrderNotFoundException;
import com.tortora.financas.model.Order;
import com.tortora.financas.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceTest {

    @MockBean
    private OrderRepository repository;

    @Autowired
    private OrderService service;

    @Test
    void getOrdersTest() {
        Order o = new Order("Minha primeira ordem", Status.IN_PROGRESS);
        Order o2 = new Order("Minha segunda ordem", Status.COMPLETED);
        List<Order> list = new ArrayList<>();
        list.add(o);
        list.add(o2);

        when(repository.findAll()).thenReturn(list);
        List<EntityModel<Order>> response = service.getOrders();
        Assertions.assertEquals(2, response.size());
    }

    @Test
    void getOrderByIdTest() {
        Optional<Order> o = Optional.of(new Order("Minha ordem", Status.IN_PROGRESS));

        when(repository.findById(1L)).thenReturn(o);
        Order response = service.getOrderById(1L);
        Assertions.assertEquals("Minha ordem", response.getDescription());
    }

    @Test
    void getOrderByIdNotFoundTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        try {
            service.getOrderById(1L);
        } catch (OrderNotFoundException e) {
            Assertions.assertEquals("Could not find order 1", e.getMessage());
        }
    }

    @Test
    void getEntityModelOrderByIdTest() {
        Optional<Order> o = Optional.of(new Order("Minha ordem", Status.IN_PROGRESS));

        when(repository.findById(1L)).thenReturn(o);
        EntityModel<Order> response = service.getEntityModelOrderById(1L);
        Assertions.assertEquals("Minha ordem", Objects.requireNonNull(response.getContent()).getDescription());
    }

    @Test
    void getEntityModelOrderByIdNotFoundTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        try {
            service.getEntityModelOrderById(1L);
        } catch (OrderNotFoundException e) {
            Assertions.assertEquals("Could not find order 1", e.getMessage());
        }
    }

    @Test
    void saveOrderTest() {
        Order o = new Order("Minha primeira ordem", Status.IN_PROGRESS);

        when(repository.save(o)).thenReturn(o);
        EntityModel<Order> response = service.saveOrder(o);
        Assertions.assertEquals(Status.IN_PROGRESS, Objects.requireNonNull(response.getContent()).getStatus());
    }

    @Test
    void cancelOrderTest() {
        Order o = new Order("Minha primeira ordem", Status.IN_PROGRESS);
        Order cancelledOrder = new Order("Minha primeira ordem", Status.CANCELLED);

        when(repository.save(o)).thenReturn(cancelledOrder);
        EntityModel<Order> response = service.cancelOrder(o);
        Assertions.assertEquals(Status.CANCELLED, Objects.requireNonNull(response.getContent()).getStatus());
    }

    @Test
    void cancelOrderWhenOrderIsCancelledTest() {
        Order o = new Order("Minha primeira ordem", Status.CANCELLED);

        EntityModel<Order> response = service.cancelOrder(o);
        Assertions.assertNull(response);
    }

    @Test
    void completeOrderTest() {
        Order o = new Order("Minha primeira ordem", Status.IN_PROGRESS);
        Order completedOrder = new Order("Minha primeira ordem", Status.COMPLETED);

        when(repository.save(o)).thenReturn(completedOrder);
        EntityModel<Order> response = service.completeOrder(o);
        Assertions.assertEquals(Status.COMPLETED, Objects.requireNonNull(response.getContent()).getStatus());
    }

    @Test
    void completeOrderWhenOrderIsCompletedTest() {
        Order o = new Order("Minha primeira ordem", Status.COMPLETED);

        EntityModel<Order> response = service.completeOrder(o);
        Assertions.assertNull(response);
    }

}
