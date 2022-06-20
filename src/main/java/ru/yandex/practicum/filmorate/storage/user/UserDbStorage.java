package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FoundException;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.LikesDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final LikesDao likesDao;
    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, LikesDao likesDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.likesDao = likesDao;
    }

    @Override
    public User create(User user) {

        String sqlQuery = "insert into users (email, login, name, birthday) " +
                "values (?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        sqlQuery = "select user_id from users order by user_id desc limit 1 ";
        long userId = jdbcTemplate.queryForObject(sqlQuery, Integer.class);

        return getById(userId);
    }

    @Override
    public List<User> findAll() {
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User update(User user) {
        idValidation(user.getId());
        String sqlQuery = "update users set name = ?, login = ?, birthday = ?, email = ?" +
                "where user_id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getLogin(),
                user.getBirthday(),
                user.getEmail(),
                user.getId());
        return getById(user.getId());
    }

    @Override
    public User getById(Long id) {
        idValidation(id);
        String sqlQuery = "select * from users where user_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeUser(rs), id);
    }

    @Override
    public void saveLike(long filmId, long userId) {
        likesDao.saveLike(filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        likesDao.deleteLike(filmId, userId);
    }

    @Override
    public void delete(String email) {
        String sqlQuery = "delete from users where email = ?";
        jdbcTemplate.update(sqlQuery, email);
    }
    @Override
    public void saveFriend(long userId, long friendId){
        String sqlQuery = "merge into friends (user_id, friend, friendship_status) key (user_id, friend)" +
                "values (?,?,?)";
        jdbcTemplate.update(sqlQuery, userId, friendId, 1);
    }
    @Override
    public void saveSubscriber(long userId, long subscriberId){
        String sqlQuery = "merge into friends (user_id, friend, friendship_status) key (user_id, friend)" +
                "values (?,?,?)";
        jdbcTemplate.update(sqlQuery, userId, subscriberId, 2);
    }
    @Override
    public void deleteFriend(long userId, long subscriberId) {
        String sqlQuery = "delete from friends where user_id = ? and friend = ?";
        jdbcTemplate.update(sqlQuery, userId, subscriberId);
    }
    @Override
    public List<Long> getUserFriends(long id) {
        String sqlQuery = "select friend from friends where user_id = ? and friendship_status = 1";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, id).stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }
    @Override
    public List<Long> getUserSubscribers(long id) {
        String sqlQuery = "select friend from friends where user_id = ? and friendship_status = 2";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, id).stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    private User makeUser(ResultSet rs) throws SQLException {
        long id = rs.getInt("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        User user = new User(id, email, login, name, birthday);
        user.getLikedFilms().addAll(likesDao.getUserLikes(id));
        user.getFriends().addAll(getUserFriends(id));
        user.getSubscribers().addAll(getUserSubscribers(id));
        return user;
    }

    private void idValidation(long id) {
        List<Integer> userIds;
        String sqlQuery = "select user_id from users";
        userIds = jdbcTemplate.queryForList(sqlQuery, Integer.class);
        if (!userIds.contains((int) id)) {
            throw new FoundException("Unknown user");
        }
    }
}