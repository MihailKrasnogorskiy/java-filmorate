package ru.yandex.practicum.filmorate.storages.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.serveses.IDCreator;
import ru.yandex.practicum.filmorate.storages.user.UserStorage;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
//класс реализация хранилища пользователей
public class InMemoryUserStorage implements UserStorage {
    private IDCreator idCreator;
    private final HashMap<String, User> users = new HashMap<>();
    private final HashMap<Long, String> emailMaps = new HashMap<>();

    public InMemoryUserStorage(IDCreator idCreator) {
        this.idCreator = idCreator;
    }

    @Override
    public User createUser(User user) {
        if (users.containsKey(user.getEmail())) {
            throw new ValidationException("This email is used");
        }
        user.setId(idCreator.generateId());
        emailMaps.put(user.getId(), user.getEmail());
        users.put(user.getEmail(), user);
        log.info("Create new user {}", user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(User user) {
        if (emailMaps.containsKey(user.getId()) && !(emailMaps.get(user.getId()).equals(user.getEmail()))) {
            users.remove(emailMaps.get(user.getId()));
        }
        emailMaps.put(user.getId(), user.getEmail());
        users.put(user.getEmail(), user);
        log.info("Update user {}", user);
        return user;
    }

}