package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class UserControllerTest {
    private final User user = new User(1,
            "mail@mail.ru",
            "TomCycyruz",
            "Tom",
            LocalDate.of(1975, 6, 2));
    private final User user1 = new User(0,
            "mail@yandex.ru",
            "AngelinaJolie",
            "Angelina",
            LocalDate.of(1975, 6, 4));

    private final User user2 = new User(2,
            "goblin@oper.ru",
            "DmitriyPuchkov",
            "Goblin",
            LocalDate.of(1961, 8, 2));
    private final User validUser = new User(1,
            "mail@mail.ru",
            "TomCycyruz",
            "Tom",
            LocalDate.of(1975, 6, 2));
    @Autowired
    ObjectMapper mapper;
    @Autowired
    UserStorage userStorage;
    private String body;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserController controller;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    //создание и обновление пользователя
    public void test5_createAndUpdateWithValidArguments() throws Exception {
        cleareStorage();
        String body = mapper.writeValueAsString(user);
        //валидный post
        postWithValidArguments(body);
        user.setId(1);
        assertEquals(user, controller.findAll().get(0));
        user.setName("new_name");
        //валидный put
        body = mapper.writeValueAsString(user);
        putWithValidArguments(body);
        assertEquals(user, controller.findAll().get(0));
        //валидный get по id
        this.mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
        assertEquals(1, controller.findAll().size());
        assertEquals(user.getEmail(), controller.findAll().get(0).getEmail());
        assertEquals(user.getName(), controller.findAll().get(0).getName());
        assertEquals(user.getId(), controller.findAll().get(0).getId());
        assertEquals(user.getBirthday(), controller.findAll().get(0).getBirthday());
    }

    @Test
    //неверный логин
    public void test6_createAndUpdateWithNotValidLogin() throws Exception {
        createEnvironment();
        //невалидный логин post
        user.setLogin("Tom Cycyruz");
        body = mapper.writeValueAsString(user);
        postWithNotValidArguments(body);
        //невалидная продлжительность put
        putWithNotValidArguments(user);
    }

    @Test
    //неверный email
    public void test7_createAndUpdateWithNotValidEmail() throws Exception {
        createEnvironment();
        user.setEmail("tomcycyruzMail.ru");
        body = mapper.writeValueAsString(user);
        //невалидное описание post
        postWithNotValidArguments(body);
        //невалидное описание put
        putWithNotValidArguments(user);
    }

    @Test
    //неверная дата рождения
    public void test8_createAndUpdateWithNotValidDate() throws Exception {
        createEnvironment();
        user.setBirthday(LocalDate.of(2026, 1, 21));
        //невалидная дата релиза post
        body = mapper.writeValueAsString(user);
        postWithNotValidArguments(body);
        //невалидное описание put
        putWithNotValidArguments(user);
    }

    @Test
    //нулевое имя
    public void test9_createAndUpdateWithNotValidName() throws Exception {
        createEnvironment();
        user.setName(null);
        //невалидная дата релиза post
        body = mapper.writeValueAsString(user);
        postWithNotValidArguments(body);
        //невалидное описание put
        putWithNotValidArguments(user);
    }

    //добавление в друзья и удаление из друзей
    @Test
    public void test10_addAndDeleteFriends() throws Exception {
        createEnvironment();
        body = mapper.writeValueAsString(user1);
        postWithValidArguments(body);
        mockMvc.perform(put("/users/1/friends/2"))
                .andExpect(status().isOk());
        final Long id1 = userStorage.getById(1L).getFriends().stream().findFirst().get();
        assertEquals(2, id1);
        mockMvc.perform(put("/users/2/friends/confirm/1"))
                .andExpect(status().isOk());
        final Long id2 = userStorage.getById(2L).getFriends().stream().findFirst().get();
        assertEquals(1, id2);
        mockMvc.perform(delete("/users/1/friends/2"))
                .andExpect(status().isOk());
        assertTrue(userStorage.getById(1L).getFriends().isEmpty());
    }

    //выдача общих друзей
    @Test
    public void test11_commonFriends() throws Exception {
        createEnvironment();
        body = mapper.writeValueAsString(user1);
        postWithValidArguments(body);
        body = mapper.writeValueAsString(user2);
        postWithValidArguments(body);
        mockMvc.perform(put("/users/1/friends/2"))
                .andExpect(status().isOk());
        body = mapper.writeValueAsString(user2);
        mockMvc.perform(put("/users/1/friends/3"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/users/2/friends/3"))
                .andExpect(status().isOk());
        user2.setId(3L);
        user2.getFriends().add(1L);
        user2.getFriends().add(2L);
        List<User> commonFriends = new ArrayList<>();
        commonFriends.add(user2);
        body = mapper.writeValueAsString(commonFriends);
        this.mockMvc.perform(get("/users/1/friends/common/2"))
                .andExpect(status().isOk());
    }

    //возвращение списка друзей
    @Test
    public void test12_getFriends() throws Exception {
        createEnvironment();
        body = mapper.writeValueAsString(user1);
        postWithValidArguments(body);
        mockMvc.perform(put("/users/1/friends/2"))
                .andExpect(status().isOk());
        user.setId(1L);
        user1.setId(2L);
        user.getFriends().add(2L);
        List<User> friends = new ArrayList<>();
        friends.add(user);
        body = mapper.writeValueAsString(friends);
        this.mockMvc.perform(get("/users/2/friends"))
                .andExpect(status().isOk());
    }

    //создание окружения
    private void createEnvironment() throws Exception {
        cleareStorage();
        validUser.setId(1);
        body = mapper.writeValueAsString(user);
        postWithValidArguments(body);
        assertEquals(1, controller.findAll().size());
        assertEquals(validUser.getEmail(), controller.findAll().get(0).getEmail());
        assertEquals(validUser.getName(), controller.findAll().get(0).getName());
        assertEquals(validUser.getId(), controller.findAll().get(0).getId());
        assertEquals(validUser.getBirthday(), controller.findAll().get(0).getBirthday());
    }

    //post запрос с валидными аргументами
    private void postWithValidArguments(String body) throws Exception {
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //post запрос с невалидными аргументами
    private void postWithNotValidArguments(String body) throws Exception {
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertEquals(1, controller.findAll().size());
        assertEquals(validUser.getEmail(), controller.findAll().get(0).getEmail());
        assertEquals(validUser.getName(), controller.findAll().get(0).getName());
        assertEquals(validUser.getId(), controller.findAll().get(0).getId());
        assertEquals(validUser.getBirthday(), controller.findAll().get(0).getBirthday());
    }

    //put запрос с валидными аргументами
    private void putWithValidArguments(String body) throws Exception {
        this.mockMvc.perform(put("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //put запрос с невалидными аргументами
    private void putWithNotValidArguments(User user) throws Exception {
        user.setId(1L);
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(put("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertEquals(1, controller.findAll().size());
        assertEquals(validUser.getEmail(), controller.findAll().get(0).getEmail());
        assertEquals(validUser.getName(), controller.findAll().get(0).getName());
        assertEquals(validUser.getId(), controller.findAll().get(0).getId());
        assertEquals(validUser.getBirthday(), controller.findAll().get(0).getBirthday());
    }

    private void cleareStorage() {
        String sqlQuery = "SET REFERENTIAL_INTEGRITY FALSE";
        jdbcTemplate.update(sqlQuery);
        sqlQuery = "TRUNCATE TABLE films ";
        jdbcTemplate.update(sqlQuery);
        sqlQuery = "TRUNCATE TABLE requests";
        jdbcTemplate.update(sqlQuery);
        sqlQuery = "TRUNCATE TABLE likes";
        jdbcTemplate.update(sqlQuery);
        sqlQuery = "TRUNCATE TABLE film_genres";
        jdbcTemplate.update(sqlQuery);
        sqlQuery = "TRUNCATE TABLE friends";
        jdbcTemplate.update(sqlQuery);
        sqlQuery = "TRUNCATE TABLE users";
        jdbcTemplate.update(sqlQuery);
        sqlQuery = "ALTER TABLE films ALTER COLUMN film_id RESTART WITH 1";
        jdbcTemplate.update(sqlQuery);
        sqlQuery = "ALTER TABLE requests ALTER COLUMN REQUEST_ID RESTART WITH 1";
        jdbcTemplate.update(sqlQuery);
        sqlQuery = "ALTER TABLE LIKES ALTER COLUMN ID RESTART WITH 1";
        jdbcTemplate.update(sqlQuery);
        sqlQuery = "ALTER TABLE FILM_GENRES ALTER COLUMN ID RESTART WITH 1";
        jdbcTemplate.update(sqlQuery);
        sqlQuery = "ALTER TABLE FRIENDS ALTER COLUMN id RESTART WITH 1";
        jdbcTemplate.update(sqlQuery);
        sqlQuery = "ALTER TABLE USERS ALTER COLUMN user_id RESTART WITH 1";
        jdbcTemplate.update(sqlQuery);
        sqlQuery = "SET REFERENTIAL_INTEGRITY TRUE";
        jdbcTemplate.update(sqlQuery);
    }
}
