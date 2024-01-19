package com.tortora.financas.controller;

import com.tortora.financas.model.User;
import com.tortora.financas.service.UserService;
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
    public @ResponseBody String addNewUser(@RequestParam String name, @RequestParam String email) {
        return service.saveUser(name, email);
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return service.getUsers();
    }

    @DeleteMapping(path="/delete")
    public @ResponseBody String deleteAllUsers() {
        return service.deleteUsers();
    }

}
