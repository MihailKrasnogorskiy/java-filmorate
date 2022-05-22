package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    ObjectMapper mapper;
    private final User user = new User(
            "mail@mail.ru",
            "TomCycyruz",
            "Tom",
            LocalDate.of(1975, 06, 02));
    private final User validUser = new User(
            "mail@mail.ru",
            "TomCycyruz",
            "Tom",
            LocalDate.of(1975, 06, 02));
    private String body;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserController controller;

    @Test
    //создание и обновление пользователя
    public void test5_createAndUpdateWithValidArguments() throws Exception {
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

    //создание окружения
    private void createEnvironment() throws Exception {
        controller.resetController();
        validUser.setId(1);
        body = mapper.writeValueAsString(user);
        postWithValidArguments(body);
        assertEquals(1, controller.findAll().size());
        assertEquals(validUser, controller.findAll().get(0));
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
        assertEquals(validUser, controller.findAll().get(0));
    }

    //put запрос с валидными аргументами
    private void putWithValidArguments(String body) throws Exception {
        this.mockMvc.perform(put("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //put запрос с невалидными аргументами
    private void putWithNotValidArguments(User user) throws Exception {
        user.setId(1);
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(put("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertEquals(1, controller.findAll().size());
        assertEquals(validUser, controller.findAll().get(0));
    }

}
