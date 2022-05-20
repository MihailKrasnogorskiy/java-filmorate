package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.user.UserStorage;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j

//REST контроллер
public class UserController {
    private UserStorage storage;

    @Autowired
    public UserController(UserStorage storage) {
        this.storage = storage;
    }

    @GetMapping
    //возвращает список всех пользователей
    public List<User> findAll() {
        return storage.findAll();
    }

    // создание пользователя
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        storage.createUser(user);
        log.info("Create new user {}", user);
        return user;
    }

    // обновление пользователя
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        storage.updateUser(user);
        log.info("Update user {}", user);
        return user;
    }
}
