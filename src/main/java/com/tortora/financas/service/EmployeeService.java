package com.tortora.financas.service;

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

    private final EmployeeRepository employeeRepository;
    private final EmployeeModelAssembler employeeModelAssembler;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeModelAssembler employeeModelAssembler) {
        this.employeeRepository = employeeRepository;
        this.employeeModelAssembler = employeeModelAssembler;
    }

    public List<EntityModel<Employee>> getEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeModelAssembler::toModel)
                .collect(Collectors.toList());
    }

    public EntityModel<Employee> saveEmployee(Employee newEmployee) {
        return employeeModelAssembler.toModel(employeeRepository.save(newEmployee));
    }

    public EntityModel<Employee> getEmployeeById(Long employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);

        if (employee.isPresent()) {
            return employeeModelAssembler.toModel(employee.get());
        } else {
            throw new EmployeeNotFoundException(employeeId);
        }
    }

    public EntityModel<Employee> updateEmployee(Employee newEmployee, Long employeeId) {
        Employee updatedEmployee = employeeRepository.findById(employeeId)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return employeeRepository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(employeeId);
                    return employeeRepository.save(newEmployee);
                });
        return employeeModelAssembler.toModel(updatedEmployee);
    }

    public void deleteEmployeeById(Long employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);

        if (employee.isPresent()) {
            employeeRepository.deleteById(employeeId);
        } else {
            throw new EmployeeNotFoundException(employeeId);
        }
    }

    public void deleteAllEmployees() {
        employeeRepository.deleteAll();
    }

}
