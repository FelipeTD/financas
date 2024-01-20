package com.tortora.financas.controller;

import com.tortora.financas.enums.Status;
import com.tortora.financas.exceptions.OrderNotFoundException;
import com.tortora.financas.model.Employee;
import com.tortora.financas.model.Order;
import com.tortora.financas.model.OrderModelAssembler;
import com.tortora.financas.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.tortora.financas.utils.TestUtils.asJsonString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService service;
    @Spy
    private OrderModelAssembler assembler;

    @Test
    void allOrdersTest() throws Exception {
        Order o = new Order("Tenis Adidas", Status.IN_PROGRESS);
        Order o2 = new Order("Iphone", Status.CANCELLED);

        List<Order> orderList = new ArrayList<>();
        orderList.add(o);
        orderList.add(o2);

        List<EntityModel<Order>> entityModelOrderList = orderList.stream() //
                .map(assembler::toModel).toList();

        when(service.getOrders()).thenReturn(entityModelOrderList);
        this.mockMvc.perform(get("/orders")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.orderList").exists())
                .andExpect(jsonPath("$._embedded.orderList[*].description").exists());
    }

    @Test
    void oneOrderTest() throws Exception {
        Order o = new Order("Tenis Adidas", Status.IN_PROGRESS);
        o.setId(1L);

        EntityModel<Order> orderEntityModel = assembler.toModel(o);

        when(service.getEntityModelOrderById(1L)).thenReturn(orderEntityModel);
        this.mockMvc.perform(get("/orders/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Tenis Adidas"));

    }

    @Test
    void oneOrderExceptionTest() throws Exception {
        when(service.getEntityModelOrderById(1L)).thenThrow(OrderNotFoundException.class);
        this.mockMvc.perform(get("/orders/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    assertEquals("{\"title\":\"Order Not Found\"}", result.getResponse().getContentAsString());
                });
    }

    @Test
    void newOrderTest() throws Exception {
        Order o = new Order("Tenis Adidas", Status.IN_PROGRESS);
        o.setId(1L);

        EntityModel<Order> orderEntityModel = assembler.toModel(o);

        when(service.saveOrder(o)).thenReturn(orderEntityModel);
        this.mockMvc.perform(post("/orders")
                        .content(asJsonString(o))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").exists());
    }

    @Test
    void cancelOrderInProgress() throws Exception {

        Order o = new Order("Tenis Adidas", Status.IN_PROGRESS);
        o.setId(1L);
        when(service.getOrderById(1L)).thenReturn(o);

        o.setStatus(Status.CANCELLED);
        EntityModel<Order> cancelledOrderEntityModel = assembler.toModel(o);
        when(service.cancelOrder(o)).thenReturn(cancelledOrderEntityModel);

        this.mockMvc.perform(delete("/orders/{id}/cancel", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void cancelOrderCancelled() throws Exception {

        Order o = new Order("Tenis Adidas", Status.CANCELLED);
        o.setId(1L);
        when(service.getOrderById(1L)).thenReturn(o);

        this.mockMvc.perform(delete("/orders/{id}/cancel", 1L))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.title").value("Method not allowed"));
    }

    @Test
    void completeOrderInProgress() throws Exception {

        Order o = new Order("Tenis Adidas", Status.IN_PROGRESS);
        o.setId(1L);
        when(service.getOrderById(1L)).thenReturn(o);

        o.setStatus(Status.COMPLETED);
        EntityModel<Order> completeOrderEntityModel = assembler.toModel(o);
        when(service.completeOrder(o)).thenReturn(completeOrderEntityModel);

        this.mockMvc.perform(put("/orders/{id}/complete", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void completeOrderCompleted() throws Exception {
        Order o = new Order("Tenis Adidas", Status.COMPLETED);
        o.setId(1L);
        when(service.getOrderById(1L)).thenReturn(o);

        this.mockMvc.perform(put("/orders/{id}/complete", 1L))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.title").value("Method not allowed"));
    }

    @Test
    void orderToStringTest() {
        Order o = new Order("Minha ordem", Status.IN_PROGRESS);
        o.setId(1L);
        Assertions.assertEquals("Order{id=1, description='Minha ordem', status=IN_PROGRESS}", o.toString());
    }

    @Test
    void orderHashCodeTest() {
        Order o = new Order("Minha ordem", null);
        o.setId(1L);
        Assertions.assertEquals(1385235724, o.hashCode());
    }

    @Test
    void orderEqualsTest() {

        // Objetos iguais
        Order o = new Order("Minha ordem", Status.IN_PROGRESS);
        o.setId(1L);
        Order o2 = new Order("Minha ordem", Status.IN_PROGRESS);
        o2.setId(1L);

        // ID diferente
        Order o3 = new Order("Minha ordem", Status.IN_PROGRESS);
        o3.setId(2L);

        // description diferente
        Order o4 = new Order("Minha segunda ordem", Status.IN_PROGRESS);
        o4.setId(1L);

        // status diferente
        Order o5 = new Order("Minha ordem", Status.COMPLETED);
        o5.setId(1L);

        // Objeto diferente
        Employee employee = new Employee("Filipe", "Tortora", "Programador");

        Assertions.assertEquals(o, o);
        Assertions.assertNotEquals(o, employee);
        Assertions.assertEquals(o, o2);
        Assertions.assertNotEquals(o, o3);
        Assertions.assertNotEquals(o, o4);
        Assertions.assertNotEquals(o, o5);
    }

}
