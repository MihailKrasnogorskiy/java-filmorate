package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.films.genre.Genres;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class GenreControllerTest {
    private final ObjectMapper mapper;

    private final MpaStorage storage;

    private final GenreController controller;
    private final MockMvc mockMvc;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreControllerTest(ObjectMapper mapper, MpaStorage storage, GenreController controller, MockMvc mockMvc,
                               JdbcTemplate jdbcTemplate) {
        this.mapper = mapper;
        this.storage = storage;
        this.controller = controller;
        this.mockMvc = mockMvc;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Test
    // получение всех рейтингов
    public void test15_getAllGenres() throws Exception {
        this.mockMvc.perform(get("/genres/"))
                .andExpect(status().isOk());
        assertEquals(6, controller.getAll().size());
        assertEquals(1, controller.getAll().get(0).getId());
        assertEquals(2, controller.getAll().get(1).getId());
        assertEquals(3, controller.getAll().get(2).getId());
        assertEquals(4, controller.getAll().get(3).getId());
        assertEquals(5, controller.getAll().get(4).getId());
        assertEquals(6, controller.getAll().get(5).getId());
        assertEquals(Genres.Комедия, controller.getAll().get(0).getName());
        assertEquals(Genres.Драма, controller.getAll().get(1).getName());
        assertEquals(Genres.Мультфильм, controller.getAll().get(2).getName());
        assertEquals(Genres.Триллер, controller.getAll().get(3).getName());
        assertEquals(Genres.Документальный, controller.getAll().get(4).getName());
        assertEquals(Genres.Боевик, controller.getAll().get(5).getName());
    }

    @Test
    // получение всех рейтингов
    public void test16_getMpaById() throws Exception {
        this.mockMvc.perform(get("/genres/1"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/genres/2"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/genres/3"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/genres/4"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/genres/5"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/genres/6"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/genres/7"))
                .andExpect(status().isNotFound());
        this.mockMvc.perform(get("/genres/0"))
                .andExpect(status().isNotFound());
        assertEquals(Genres.Комедия, controller.findGenreById(1).getName());
        assertEquals(Genres.Драма, controller.findGenreById(2).getName());
        assertEquals(Genres.Мультфильм, controller.findGenreById(3).getName());
        assertEquals(Genres.Триллер, controller.findGenreById(4).getName());
        assertEquals(Genres.Документальный, controller.findGenreById(5).getName());
        assertEquals(Genres.Боевик, controller.findGenreById(6).getName());
    }
}
