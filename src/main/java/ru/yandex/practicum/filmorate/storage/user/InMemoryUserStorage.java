package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FoundException;
import ru.yandex.practicum.filmorate.model.users.Request;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.services.IdCreator;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@Primary
//класс реализация хранилища пользователей
public class InMemoryUserStorage implements UserStorage {
    private final IdCreator userIdCreator = new IdCreator();
    private final HashMap<String, User> users = new HashMap<>();
    private final HashMap<Long, String> emailMaps = new HashMap<>();
    private final HashMap<Long, Request> requestMap = new HashMap<>();

    private int requestCounter;

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
    public void saveFriend(long outgoing, long incoming, Request request) {
        getById(outgoing).getFriends().add(incoming);
    }

    @Override
    public void deleteFriend(long outgoingId, long incomingId) {
        Request request = getRequest(incomingId,outgoingId);
        String oldRequestStatus = request.getStatus();
        request.setStatus("delete");
        requestMap.put(request.getId(), request);
        if (oldRequestStatus.equals("confirm")) {
            getById(incomingId).getFriends().remove(outgoingId);
        }
        getById(outgoingId).getFriends().remove(incomingId);
    }

    @Override
    public List<Long> getUserFriends(long id) {
        return new ArrayList<>(getById(id).getFriends());
    }

    @Override
    public void confirmRequest(long incomingId, long outgoingId) {
        Request request = getRequest(incomingId,outgoingId);
        request.setStatus("confirm");
        requestMap.put(request.getId(), request);
        saveFriend(incomingId, outgoingId, request);
    }

    @Override
    public Request saveRequest(Request request) {
        requestCounter++;
        request.setId(requestCounter);
        requestMap.put(request.getId(), request);
        return request;
    }

    @Override
    public void userIdValidation(long id) {
        if(!emailMaps.containsKey(id)){
            throw new FoundException("This user unregistered");
        }
    }

    private Request getRequest(long incomingId, long outgoingId){
        return requestMap.values().stream()
                .map(Request::getIncomingId)
                .filter(s -> s == incomingId)
                .map(requestMap::get)
                .map(Request::getOutgoingId)
                .filter(s -> s == outgoingId)
                .map(requestMap::get)
                .findFirst()
                .get();
    }

    //очистка хранилища
    public void clear() {
        users.clear();
        userIdCreator.clear();
        requestCounter = 0;
        requestMap.clear();
    }
}