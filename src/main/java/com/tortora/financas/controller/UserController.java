package com.tortora.financas.controller;

import com.tortora.financas.model.User;
import com.tortora.financas.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping(path="/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody String addNewUser(@RequestParam String name, @RequestParam String email) {
        return service.saveUser(name, email);
    }

    @GetMapping(path="/all")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Iterable<User> getAllUsers() {
        return service.getUsers();
    }

    @DeleteMapping(path="/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody String deleteAllUsers() {
        return service.deleteUsers();
    }

}
