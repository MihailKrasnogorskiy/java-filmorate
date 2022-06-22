package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.users.Request;
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

    //удаление пользователя по email
    void delete(String email);

    //сохранение друга
    void saveFriend(long userId, long friendId, Request request);

    //удаление друга
    void deleteFriend(long userId, long subscriberId);

    //возвращение списка друзей
    List<Long> getUserFriends(long id);

    void confirmRequest(long incomingId, long outgoingId);

    // сохранение заявки
    Request saveRequest(Request request);

    void userIdValidation(long id);
}
