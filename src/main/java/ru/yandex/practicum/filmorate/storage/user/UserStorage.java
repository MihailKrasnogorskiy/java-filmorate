package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    //добавление пользователя
    User createUser(User user);

    //поиск всех пользователей
    List<User> findAll();

    //обновление пользователя
    User updateUser(User user);

    //поиск пользователя по id
    User getUserById(Long id);

    //удаление пользователя по email
    void deleteUser(String email);
}
