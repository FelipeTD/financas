package com.tortora.financas.repository;

import com.tortora.financas.enums.Status;
import com.tortora.financas.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.status = :status")
    Collection<Order> findOrderByStatus(@Param("status") Status status);

}
