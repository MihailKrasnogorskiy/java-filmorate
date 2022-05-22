package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.servises.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j

//REST контроллер
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    //возвращает список всех пользователей
    public List<User> findAll() {
        return service.findAll();
    }

    // создание пользователя
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        User user1 = service.createUser(user);
        log.info("Create new user {}", user);
        return user1;
    }

    // обновление пользователя
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        User user1 = service.updateUser(user);
        log.info("Update user {}", user);
        return user1;
    }

    //добавление в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, Long friendId) {
        service.addFriendToFriendsSet(id, friendId);
    }

    //удаление из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, Long friendId) {
        service.deleteFriendToFriendsSet(id, friendId);
    }

    //возвращаем список пользователей, являющихся его друзьями
    @GetMapping("/{id}/friends")
    public List<User> findFriends(@PathVariable Long id) {
        return service.getFriends(id);
    }

    //список друзей, общих с другим пользователем
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable Long id1, Long id2) {
        return service.findCommonFriends(id1, id2);
    }
}
