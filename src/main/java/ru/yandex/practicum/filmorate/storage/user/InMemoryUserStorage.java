package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.servises.User.UserIdCreator;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
//класс реализация хранилища пользователей
public class InMemoryUserStorage implements UserStorage {
    private final UserIdCreator userIdCreator;
    private final HashMap<String, User> users = new HashMap<>();
    private final HashMap<Long, String> emailMaps = new HashMap<>();

    @Autowired
    public InMemoryUserStorage(UserIdCreator userIdCreator) {
        this.userIdCreator = userIdCreator;
    }


    @Override
    public User createUser(User user) {
        if (users.containsKey(user.getEmail())) {
            throw new ValidationException("This email is used");
        }
        user.setId(userIdCreator.generateId());
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

    @Override
    public User getUserById(Long id) {
        if (!emailMaps.containsKey(id) || id <= 0) {
            throw new FoundException("User is not registered");
        }
        return users.get(emailMaps.get(id));
    }

    @Override
    public void deleteUser(String email) {
        if (!users.containsKey(email) || email.equals("")) {
            throw new FoundException("User is not registered");
        }
        emailMaps.remove(users.get(email).getId());
        users.remove(email);
        log.info("Delete user with email {}", email);
    }

    public void clear() {
        users.clear();
        userIdCreator.clear();
    }
}