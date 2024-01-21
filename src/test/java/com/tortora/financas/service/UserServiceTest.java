package com.tortora.financas.service;

import com.tortora.financas.model.User;
import com.tortora.financas.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository repository;

    @Autowired
    private UserService service;

    @Test
    void saveUserTest() {
        User user = new User();
        user.setName("Filipe");
        user.setEmail("fedispato@gmail.com");

        when(repository.save(user)).thenReturn(user);
        String response = service.saveUser("Filipe", "fedispato@gmail.com");
        Assertions.assertEquals("Saved", response);
    }

    @Test
    void getUsersTest() {
        User user = new User();
        user.setName("Filipe");
        user.setEmail("fedispato@gmail.com");

        User user2 = new User();
        user2.setName("Filipe");
        user2.setEmail("fedispato@gmail.com");

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        when(repository.findAll()).thenReturn(users);
        Iterable<User> response = service.getUsers();
        Assertions.assertNotNull(response);
    }

    @Test
    void deleteUsersTest() {
        String response = service.deleteUsers();
        Assertions.assertEquals("Deleted", response);

    }

}
