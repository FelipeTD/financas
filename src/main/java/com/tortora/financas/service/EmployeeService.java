package com.tortora.financas.service;

import com.tortora.financas.exceptions.EmployeeNotFoundException;
import com.tortora.financas.model.Employee;
import com.tortora.financas.model.EmployeeModelAssembler;
import com.tortora.financas.model.request.MigrateEmployeeRequest;
import com.tortora.financas.repository.EmployeeRepository;
import com.tortora.financas.utils.ConverterUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeModelAssembler employeeModelAssembler;
    private final ConverterUtils converterUtils;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeModelAssembler employeeModelAssembler, ConverterUtils converterUtils) {
        this.employeeRepository = employeeRepository;
        this.employeeModelAssembler = employeeModelAssembler;
        this.converterUtils = converterUtils;
    }

    public List<EntityModel<Employee>> getEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeModelAssembler::toModel)
                .collect(Collectors.toList());
    }

    public EntityModel<Employee> saveEmployee(Employee newEmployee) {
        return employeeModelAssembler.toModel(employeeRepository.save(newEmployee));
    }

    public List<EntityModel<Employee>> migrateEmployees(MigrateEmployeeRequest request) {
        List<Employee> employees = request.getDataType().equalsIgnoreCase("JSON")
                ? converterUtils.convertJsonEmployees(request.getUrl())
                : converterUtils.convertXMLEmployees(request.getUrl());

        employees.forEach(employeeRepository::save);

        return employees.stream()
                .map(employeeModelAssembler::toModel)
                .collect(Collectors.toList());
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
