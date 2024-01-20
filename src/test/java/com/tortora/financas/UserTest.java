package com.tortora.financas;

import com.tortora.financas.controller.UserController;
import com.tortora.financas.model.User;
import com.tortora.financas.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Test
    void addNewUserTest() throws Exception {

        when(service.saveUser("Filipe", "fedispato@gmail.com")).thenReturn("Saved");
        this.mockMvc.perform(post("/user/add?name=Filipe&email=fedispato@gmail.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Saved")));

    }

    @Test
    void getAllUsersTest() throws Exception {

        User user = new User();
        user.setName("Filipe");
        user.setEmail("fedispato@gmail.com");

        User user2 = new User();
        user2.setName("Debora");
        user2.setEmail("debora@gmail.com");

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        when(service.getUsers()).thenReturn(users);
        this.mockMvc.perform(get("/user/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[{\"id\":null,\"name\":\"Filipe\",\"email\":\"fedispato@gmail.com\"},{\"id\":null,\"name\":\"Debora\",\"email\":\"debora@gmail.com\"}]")));

    }

    @Test
    void deleteAllUsersTest() throws Exception {

        when(service.deleteUsers()).thenReturn("Deleted");
        this.mockMvc.perform(delete("/user/delete"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Deleted")));

    }

}
