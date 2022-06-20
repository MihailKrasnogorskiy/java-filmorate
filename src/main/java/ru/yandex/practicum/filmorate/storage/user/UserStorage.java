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

    //удаление пользователя по email
    void delete(String email);

    //сохранение друга
    void saveFriend(long userId, long friendId);

    //сохранение подписчика
    void saveSubscriber(long userId, long subscriberId);

    //удаление друга
    void deleteFriend(long userId, long subscriberId);

    //возвращение списка друзей
    List<Long> getUserFriends(long id);

    //возвращение списка подписчиков
    List<Long> getUserSubscribers(long id);
}
