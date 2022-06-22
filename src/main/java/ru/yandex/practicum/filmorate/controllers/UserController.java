package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.services.User.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j

//REST контроллер
public class UserController {
    private final UserService service;
    private final UserStorage storage;

    @Autowired
    public UserController(UserService service, UserStorage storage) {
        this.service = service;
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
        return storage.create(user);
    }

    // обновление пользователя
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return storage.update(user);
    }

    //добавление в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        service.addFriendToFriendsSet(id, friendId);
    }

    //удаление из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        service.deleteFriendToFriendsSet(id, friendId);
    }

    //возвращаем список пользователей, являющихся его друзьями
    @GetMapping("/{id}/friends")
    public List<User> findFriends(@PathVariable Long id) {
        return service.getFriends(id);
    }

    //список друзей, общих с другим пользователем
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return service.findCommonFriends(id, otherId);
    }

    //возвращаем пользователя по id
    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id) {
        return storage.getById(id);
    }

    //подтверждение заявки в друзья
    @PutMapping("/{id}/friends/confirm/{friendId}")
    public void confirmFriend(@PathVariable Long id, @PathVariable Long friendId) {
        service.confirmRequest(id, friendId);
    }
}
