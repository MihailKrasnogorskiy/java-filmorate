package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    private Film film = new Film(
            "Mission impossible",
            "Film with Tom Cycyruz",
            LocalDate.of(2000, 02, 22),
            Duration.ofMinutes(200));
    private Film validFilm = new Film(
            "Mission impossible",
            "Film with Tom Cycyruz",
            LocalDate.of(2000, 02, 22),
            Duration.ofMinutes(200));
    private String body;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FilmController controller;

    @Test
    // создание и обновление фильма
    public void test1_createAndUpdateWithValidArguments() throws Exception {
        String body = mapper.writeValueAsString(film);
        //валидный post
        postWithValidArguments(body);
        film.setId(1);
        assertEquals(film, controller.findAll().get(0));
        film.setDescription("new_Description");
        //валидный put
        body = mapper.writeValueAsString(film);
        putWithValidArguments(body);
        assertEquals(film, controller.findAll().get(0));
    }

    @Test
    //неверная продолжительность
    public void test2_createAndUpdateWithNotValidDuration() throws Exception {
        createEnvironment();
        //невалидная продлжительность post
        film.setDuration(Duration.ZERO);
        body = mapper.writeValueAsString(film);
        postWithNotValidArguments(body);
        film.setDuration(Duration.ofMinutes(-30));
        body = mapper.writeValueAsString(film);
        postWithNotValidArguments(body);
        //невалидная продлжительность put
        putWithNotValidArguments(film);
    }

    @Test
    //неверное описание
    public void test3_createAndUpdateWithNotValidDescription() throws Exception {
        createEnvironment();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 201; i++) {
            sb.append("a");
        }
        film.setDescription(sb.toString());
        body = mapper.writeValueAsString(film);
        //невалидное описание post
        postWithNotValidArguments(body);
        //невалидное описание put
        putWithNotValidArguments(film);
    }

    @Test
    //неверная дата релиза
    public void test4_createAndUpdateWithNotValidDate() throws Exception {
        createEnvironment();
        film.setReleaseDate(LocalDate.of(1856, 1, 21));
        //невалидная дата релиза post
        body = mapper.writeValueAsString(film);
        postWithNotValidArguments(body);
        //невалидное описание put
        putWithNotValidArguments(film);
    }

    //создание окружения
    private void createEnvironment() throws Exception {
        controller.getFilms().clear();
        controller.setId(1);
        validFilm.setId(1);
        body = mapper.writeValueAsString(film);
        postWithValidArguments(body);
        assertEquals(1, controller.findAll().size());
        assertEquals(validFilm, controller.findAll().get(0));
    }

    //post запрос с валидными аргументами
    private void postWithValidArguments(String body) throws Exception {
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //post запрос с невалидными аргументами
    private void postWithNotValidArguments(String body) throws Exception {
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertEquals(1, controller.findAll().size());
        assertEquals(validFilm, controller.findAll().get(0));
    }

    //put запрос с валидными аргументами
    private void putWithValidArguments(String body) throws Exception {
        this.mockMvc.perform(put("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //put запрос с невалидными аргументами
    private void putWithNotValidArguments(Film film) throws Exception {
        film.setId(1);
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(put("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertEquals(1, controller.findAll().size());
        assertEquals(validFilm, controller.findAll().get(0));
    }
}
