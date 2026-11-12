package com.ntq.training.userapi.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * API tests for /api/users. Do not change these tests: fix the application code instead.
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserCrudTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createUser_returnsCreatedUser() throws Exception {
        String body = """
                {
                  "username": "dung.pham",
                  "fullName": "Pham Van Dung",
                  "email": "dung.pham@example.com",
                  "password": "fake-pass-004"
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").value("dung.pham"))
                .andExpect(jsonPath("$.email").value("dung.pham@example.com"));
    }

    @Test
    void findByUsername_returnsMatchingUser() throws Exception {
        mockMvc.perform(get("/api/users/by-username/{username}", "binh.tran"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("binh.tran"))
                .andExpect(jsonPath("$.fullName").value("Tran Thi Binh"));
    }

    @Test
    void updateUnknownUser_returns404() throws Exception {
        String body = """
                {
                  "fullName": "Nobody",
                  "email": "nobody@example.com"
                }
                """;

        mockMvc.perform(put("/api/users/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUser_doesNotExposePassword() throws Exception {
        mockMvc.perform(get("/api/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("an.nguyen"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }
}
