package com.tortora.financas.controller;

import java.util.List;

import com.tortora.financas.model.Employee;
import com.tortora.financas.model.request.MigrateEmployeeRequest;
import com.tortora.financas.service.EmployeeService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<Employee>> allEmployees() {
        List<EntityModel<Employee>> employees = employeeService.getEmployees();
        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class)
                .allEmployees())
                .withSelfRel());
    }

    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> addNewEmployee(@RequestBody Employee newEmployee) {
        EntityModel<Employee> entityModel = employeeService.saveEmployee(newEmployee);
        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @PostMapping("/employees/migrate")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> migrateEmployees(@RequestBody MigrateEmployeeRequest request) {
        List<EntityModel<Employee>> employees = employeeService.migrateEmployees(request);
        return ResponseEntity.ok(CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class)
                .migrateEmployees(request))
                .withSelfRel()));
    }

    @GetMapping("/employees/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Employee> oneEmployeeById(@PathVariable Long employeeId) {
        return employeeService.getEmployeeById(employeeId);
    }

    @PutMapping("/employees/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long employeeId) {
        EntityModel<Employee> entityModel = employeeService.updateEmployee(newEmployee, employeeId);
        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @DeleteMapping("/employees/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<?> deleteEmployee(@PathVariable Long employeeId) {
        employeeService.deleteEmployeeById(employeeId);
        return ResponseEntity.noContent().build();
    }

}
