package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FoundException;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.services.IdCreator;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
//класс реализация хранилища пользователей
public class InMemoryUserStorage implements UserStorage {
    private final IdCreator userIdCreator = new IdCreator();
    private final HashMap<String, User> users = new HashMap<>();
    private final HashMap<Long, String> emailMaps = new HashMap<>();

    //создание пользователя
    @Override
    public User create(User user) {
        if (users.containsKey(user.getEmail())) {
            throw new ValidationException("This email is used");
        }
        user.setId(userIdCreator.generateId());
        emailMaps.put(user.getId(), user.getEmail());
        users.put(user.getEmail(), user);
        log.info("Create new user {}", user);
        return user;
    }

    //возвращение всех пользователей
    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    //обновление пользователя
    @Override
    public User update(User user) {
        if (user.getId() <= 0) {
            throw new FoundException("This user is not registered");
        }
        if (emailMaps.containsKey(user.getId()) && !(emailMaps.get(user.getId()).equals(user.getEmail()))) {
            users.remove(emailMaps.get(user.getId()));
        }
        emailMaps.put(user.getId(), user.getEmail());
        users.put(user.getEmail(), user);
        log.info("Update user {}", user);
        return user;
    }

    //возвращение пользователя по id
    @Override
    public User getById(Long id) {
        if (!emailMaps.containsKey(id) || id <= 0) {
            throw new FoundException("User is not registered");
        }
        return users.get(emailMaps.get(id));
    }

    //удалени пользователя
    @Override
    public void delete(String email) {
        if (!users.containsKey(email) || email.equals("")) {
            throw new FoundException("User is not registered");
        }
        emailMaps.remove(users.get(email).getId());
        users.remove(email);
        log.info("Delete user with email {}", email);
    }

    @Override
    public void saveFriend(long userId, long friendId) {

    }

    @Override
    public void saveSubscriber(long userId, long subscriberId) {

    }

    @Override
    public void deleteFriend(long userId, long subscriberId) {

    }

    @Override
    public List<Long> getUserFriends(long id) {
        return null;
    }

    @Override
    public List<Long> getUserSubscribers(long id) {
        return null;
    }

    //очистка хранилища
    public void clear() {
        users.clear();
        userIdCreator.clear();
    }
}