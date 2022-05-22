package ru.yandex.practicum.filmorate.servises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public void addFriendToFriendsSet(Long id1, Long id2) {
        User user1 = storage.getUserById(id1);
        User user2 = storage.getUserById(id2);
        validationUsers(user1, user2);
        user1.getFriends().add(user2.getId());
        user2.getFriends().add(user1.getId());
    }

    public void deleteFriendToFriendsSet(Long id1, Long id2) {
        User user1 = storage.getUserById(id1);
        User user2 = storage.getUserById(id2);
        validationUsers(user1, user2);
        user1.getFriends().remove(user2.getId());
        user2.getFriends().remove(user1.getId());
    }

    public List<User> findCommonFriends(Long id1, Long id2) {
        User user1 = storage.getUserById(id1);
        User user2 = storage.getUserById(id2);
        Set<Long> commonFriends = new HashSet<>(user1.getFriends());
        commonFriends.retainAll(user2.getFriends());

        return commonFriends.stream()
                .map(storage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> findAll() {
        return storage.findAll();
    }

    public User createUser(User user) {
        return storage.createUser(user);
    }

    public User updateUser(User user) {
        return storage.updateUser(user);
    }

    public List<User> getFriends(Long id) {
        return storage.getUserById(id).getFriends().stream()
                .map(storage::getUserById)
                .collect(Collectors.toList());
    }

    private void validationUsers(User user1, User user2) {
        if (user1.equals(user2)) {
            throw new ValidationException("User can't add or delete himself as \"friend\"");
        }
    }

}
