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
    public void addFriendToFriendsSet(long user_id, long friendId) {
        validationUsers(user_id, friendId);
        storage.saveFriend(user_id, friendId);
        //        user1.getFriends().add(user2.getId());
        //        user2.getFriends().add(user1.getId());
        log.info("User id: {} add friends user id: {}", user_id, friendId);
    }

    // удаление пользователя из друзей
    public void deleteFriendToFriendsSet(long userId, long friendId) {
//        User user1 = storage.getById(userId);
//        User user2 = storage.getById(friendId);
        validationUsers(userId, friendId);
        storage.deleteFriend(userId, friendId);
        //        user1.getFriends().remove(user2.getId());
        //        user2.getFriends().remove(user1.getId());
        log.info("User id: {} delete friends user id: {}", userId, friendId);
    }

    // вывод общих друзей
    public List<User> findCommonFriends(Long userId, Long friendId) {
        User user = storage.getById(userId);
        User friend = storage.getById(friendId);
        Set<Long> commonFriends = new HashSet<>(user.getFriends());
        commonFriends.retainAll(friend.getFriends());

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
    private void validationUsers(long userId, long friendId) {
        if (userId == friendId) {
            throw new ValidationException("User can't add or delete himself as \"friend\"");
        }
    }
}
