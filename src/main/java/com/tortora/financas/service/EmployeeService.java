package com.tortora.financas.service;

import com.tortora.financas.exceptions.EmployeeNotFoundException;
import com.tortora.financas.model.Employee;
import com.tortora.financas.model.EmployeeModelAssembler;
import com.tortora.financas.repository.EmployeeRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeModelAssembler assembler;

    public EmployeeService(EmployeeRepository repository, EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    public List<EntityModel<Employee>> getEmployees() {
        return repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }

    public EntityModel<Employee> saveEmployee(Employee newEmployee) {
        return assembler.toModel(repository.save(newEmployee));
    }

    public EntityModel<Employee> getEmployeeById(Long id) {
        Employee employee = repository.findById(id) //
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return assembler.toModel(employee);
    }

    public EntityModel<Employee> updateEmployee(Employee newEmployee, Long id) {
        Employee updatedEmployee = repository.findById(id) //
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                }) //
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
        return assembler.toModel(updatedEmployee);
    }

    public void deleteEmployeeById(Long id) {
        repository.deleteById(id);
    }

}
