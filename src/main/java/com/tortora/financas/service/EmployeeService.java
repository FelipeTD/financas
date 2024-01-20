package com.tortora.financas.service;

import com.tortora.financas.exceptions.CustomerNotFoundException;
import com.tortora.financas.exceptions.EmployeeNotFoundException;
import com.tortora.financas.model.Employee;
import com.tortora.financas.model.EmployeeModelAssembler;
import com.tortora.financas.repository.EmployeeRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        Optional<Employee> employee = repository.findById(id);

        if (employee.isPresent()) {
            return assembler.toModel(employee.get());
        } else {
            throw new EmployeeNotFoundException(id);
        }
    }

    public EntityModel<Employee> updateEmployee(Employee newEmployee, Long id) {
        Employee updatedEmployee = repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
        return assembler.toModel(updatedEmployee);
    }

    public void deleteEmployeeById(Long id) {
        Optional<Employee> employee = repository.findById(id);

        if (employee.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new EmployeeNotFoundException(id);
        }
    }

}
