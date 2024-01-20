package com.tortora.financas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;

    protected Customer() {}

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer{id=%d, firstName='%s', lastName='%s'}",
                id, firstName, lastName);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object c) {

        if (this == c)
            return true;
        if (!(c instanceof Customer))
            return false;
        Customer customer = (Customer) c;
        return Objects.equals(this.id, customer.id)
                && Objects.equals(this.firstName, customer.firstName)
                && Objects.equals(this.lastName, customer.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.firstName, this.lastName);
    }

}