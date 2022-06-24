package ru.yandex.practicum.filmorate.services.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.users.Request;
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
    public void addFriendToFriendsSet(long outgoingId, long incomingId) {
        validationUsers(outgoingId, incomingId);
        Request request = new Request(outgoingId, incomingId, "unconfirmed");
        request = storage.saveRequest(request);
        storage.saveFriend(outgoingId, incomingId, request);
        log.info("User id: {} add friends user id: {}", outgoingId, incomingId);
    }

    // удаление пользователя из друзей
    public void deleteFriendToFriendsSet(long outgoingId, long incomingId) {
        validationUsers(outgoingId, incomingId);
        storage.deleteFriend(outgoingId, incomingId);
        log.info("User id: {} delete friends user id: {}", outgoingId, incomingId);
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
        storage.userIdValidation(userId);
        storage.userIdValidation(friendId);
    }

    //подтверждение заявки в друзья
    public void confirmRequest(long incomingId, long outgoingId) {
        storage.userIdValidation(incomingId);
        storage.userIdValidation(outgoingId);
        storage.confirmRequest(incomingId, outgoingId);
    }
}
