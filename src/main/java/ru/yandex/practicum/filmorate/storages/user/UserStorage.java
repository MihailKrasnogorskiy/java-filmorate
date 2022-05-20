package ru.yandex.practicum.filmorate.storages.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    //добавление пользователя
    public User createUser(User user);
    //поиск всех пользователей
    public List<User> findAll();
//обновление пользователя
    public User updateUser(User user);

}
