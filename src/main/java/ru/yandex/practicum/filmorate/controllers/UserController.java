package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class UserController {

    private HashMap<String, User> users = new HashMap<>();

    Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public List<User> findAll() {
        log.trace("Количестов пользователей: {}", users.entrySet().size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (users.containsKey(user.getEmail())) {
            throw new ValidationException("This email is used");
        }
        log.trace(user.toString());
        users.put(user.getEmail(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        users.put(user.getEmail(), user);
        return user;
    }
}
