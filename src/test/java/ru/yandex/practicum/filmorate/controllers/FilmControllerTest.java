package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    private final Film film = new Film(
            "Mission impossible",
            "Film with Tom Cycyruz",
            LocalDate.of(2000, 2, 22),
            200);

    private final Film film1 = new Film(
            "Mission impossible2",
            "Film with Tom Cycyruz",
            LocalDate.of(2002, 4, 22),
            20);
    private final User user = new User(
            "mail@mail.ru",
            "TomCycyruz",
            "Tom",
            LocalDate.of(1975, 6, 2));

    private final Film validFilm = new Film(
            "Mission impossible",
            "Film with Tom Cycyruz",
            LocalDate.of(2000, 2, 22),
            200);
    @Autowired
    ObjectMapper mapper;
    @Autowired
    InMemoryFilmStorage storage;
    @Autowired
    InMemoryUserStorage userStorage;
    private String body;
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
        //валидный get по id
        this.mockMvc.perform(get("/films/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    //неверная продолжительность
    public void test2_createAndUpdateWithNotValidDuration() throws Exception {
        createEnvironment();
        //невалидная продлжительность post
        film.setDuration(0);
        body = mapper.writeValueAsString(film);
        postWithNotValidArguments(body);
        film.setDuration(-30);
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

    //отметка и удаление отметки понравившемуся фильму
    @Test
    public void test13_addAndDeleteLike() throws Exception {
        createEnvironment();
        mockMvc.perform(put("/films/1/like/1"))
                .andExpect(status().isOk());
        final Long filmId = userStorage.getById(1L).getLikedFilms().stream().findFirst().get();
        final Long userId = storage.getById(1L).getLikes().stream().findFirst().get();
        assertFalse(userStorage.getById(1L).getLikedFilms().isEmpty());
        assertEquals(1, userStorage.getById(1L).getLikedFilms().size());
        assertEquals(1, userId);
        assertFalse(storage.getById(1L).getLikes().isEmpty());
        assertEquals(1, storage.getById(1L).getLikes().size());
        assertEquals(1, filmId);
        mockMvc.perform(delete("/films/1/like/1"))
                .andExpect(status().isOk());
        assertTrue(userStorage.getById(1L).getLikedFilms().isEmpty());
        assertTrue(storage.getById(1L).getLikes().isEmpty());
    }

    //возвращение фильмов по популярности
    @Test
    public void test14_getPopularFilms() throws Exception {
        createEnvironment();
        mockMvc.perform(put("/films/1/like/1"))
                .andExpect(status().isOk());
        body = mapper.writeValueAsString(film1);
        postWithValidArguments(body);
        film.setId(1);
        film.getLikes().add(1L);
        film1.setId(2);
        List<Film> sorted = new ArrayList<>();
        sorted.add(film);
        sorted.add(film1);
        body = mapper.writeValueAsString(sorted);
        mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
        mockMvc.perform(get("/films/popular?count=2"))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
        sorted.remove(1);
        body = mapper.writeValueAsString(sorted);
        mockMvc.perform(get("/films/popular?count=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(body));

    }

    //создание окружения
    private void createEnvironment() throws Exception {
        storage.clear();
        userStorage.clear();
        validFilm.setId(1);
        storage.clear();
        body = mapper.writeValueAsString(film);
        postWithValidArguments(body);
        body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        user.setId(1L);
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
