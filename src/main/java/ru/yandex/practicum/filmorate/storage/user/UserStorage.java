package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.users.User;

import java.util.List;

public interface UserStorage {
    //добавление пользователя
    User create(User user);

    //поиск всех пользователей
    List<User> findAll();

    //обновление пользователя
    User update(User user);

    //поиск пользователя по id
    User getById(Long id);

    void saveLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    //удаление пользователя по email
    void delete(String email);
}
