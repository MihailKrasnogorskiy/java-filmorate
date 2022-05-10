package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

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
    private long id = 1;
    private final HashMap<String, User> users = new HashMap<>();
    private final HashMap<Long, String> emailMaps = new HashMap<>();

    @GetMapping
    //возвращает список всех пользователей
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    // создание пользователя
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (users.containsKey(user.getEmail())) {
            throw new ValidationException("This email is used");
        }
        user.setId(generateId());
        emailMaps.put(user.getId(), user.getEmail());
        users.put(user.getEmail(), user);
        log.info("Create new user {}", user);
        return user;
    }

    // обновление пользователя
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (emailMaps.containsKey(user.getId()) && !(emailMaps.get(user.getId()).equals(user.getEmail()))) {
            users.remove(emailMaps.get(user.getId()));
        }
        emailMaps.put(user.getId(), user.getEmail());
        users.put(user.getEmail(), user);
        log.info("Update user {}", user);
        return user;
    }

    // создание id
    private long generateId() {
        return id++;
    }

    public void resetController(){
        id = 1;
        users.clear();
        emailMaps.clear();
    }
}
