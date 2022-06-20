package ru.yandex.practicum.filmorate.services.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//сервис класс для обработки бизнеслогики связанной с пользователями
@Service
@Slf4j
public class UserService {

    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    //добавление пользователя в друзья
    public void addFriendToFriendsSet(Long id1, Long id2) {
        User user1 = storage.getById(id1);
        User user2 = storage.getById(id2);
        validationUsers(user1, user2);
        storage.saveFriend(id1, id2);
        user1.getFriends().add(user2.getId());
        user2.getFriends().add(user1.getId());
        log.info("User id: {} add friends user id: {}", id1, id2);
    }

    // удаление пользователя из друзей
    public void deleteFriendToFriendsSet(Long id1, Long id2) {
        User user1 = storage.getById(id1);
        User user2 = storage.getById(id2);
        validationUsers(user1, user2);
        storage.deleteFriend(id1,id2);
        user1.getFriends().remove(user2.getId());
        user2.getFriends().remove(user1.getId());
        log.info("User id: {} delete friends user id: {}", id1, id2);
    }

    // вывод общих друзей
    public List<User> findCommonFriends(Long id1, Long id2) {
        User user1 = storage.getById(id1);
        User user2 = storage.getById(id2);
        Set<Long> commonFriends = new HashSet<>(user1.getFriends());
        commonFriends.retainAll(user2.getFriends());

        return commonFriends.stream()
                .map(storage::getById)
                .collect(Collectors.toList());
    }

    //возвращение друзей
    public List<User> getFriends(Long id) {
        return storage.getById(id).getFriends().stream()
                .map(storage::getById)
                .collect(Collectors.toList());
    }

    //проверка, что в переданы id различных пользователей
    private void validationUsers(User user1, User user2) {
        if (user1.equals(user2)) {
            throw new ValidationException("User can't add or delete himself as \"friend\"");
        }
    }
}
