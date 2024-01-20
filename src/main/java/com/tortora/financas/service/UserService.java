package com.tortora.financas.service;

import com.tortora.financas.model.User;
import com.tortora.financas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public String saveUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        repository.save(user);
        return "Saved";
    }

    public Iterable<User> getUsers() {
        return repository.findAll();
    }

    public String deleteUsers() {
        repository.deleteAll();
        return "Deleted";
    }

}
