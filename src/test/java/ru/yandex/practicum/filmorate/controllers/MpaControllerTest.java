package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class MpaControllerTest {
    private final ObjectMapper mapper;

    private final MpaStorage storage;

    private final MpaController controller;
    private final MockMvc mockMvc;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaControllerTest(ObjectMapper mapper, MpaStorage storage, MpaController controller, MockMvc mockMvc,
                             JdbcTemplate jdbcTemplate) {
        this.mapper = mapper;
        this.storage = storage;
        this.controller = controller;
        this.mockMvc = mockMvc;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Test
    // получение всех рейтингов
    public void test13_getAllMpa() throws Exception {
        this.mockMvc.perform(get("/mpa/"))
                .andExpect(status().isOk());
        assertEquals(5, controller.getAll().size());
        assertEquals(1, controller.getAll().get(0).getId());
        assertEquals(2, controller.getAll().get(1).getId());
        assertEquals(3, controller.getAll().get(2).getId());
        assertEquals(4, controller.getAll().get(3).getId());
        assertEquals(5, controller.getAll().get(4).getId());
        assertEquals("G", controller.getAll().get(0).getName());
        assertEquals("PG", controller.getAll().get(1).getName());
        assertEquals("PG-13", controller.getAll().get(2).getName());
        assertEquals("R", controller.getAll().get(3).getName());
        assertEquals("NC-17", controller.getAll().get(4).getName());
    }

    @Test
    // получение всех рейтингов
    public void test14_getMpaById() throws Exception {
        this.mockMvc.perform(get("/mpa/1"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/mpa/2"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/mpa/3"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/mpa/4"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/mpa/5"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/mpa/6"))
                .andExpect(status().isNotFound());
        this.mockMvc.perform(get("/mpa/0"))
                .andExpect(status().isNotFound());
        assertEquals("G", controller.findMpaById(1).getName());
        assertEquals("PG", controller.findMpaById(2).getName());
        assertEquals("PG-13", controller.findMpaById(3).getName());
        assertEquals("R", controller.findMpaById(4).getName());
        assertEquals("NC-17", controller.findMpaById(5).getName());
    }
}
