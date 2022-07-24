package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

// класс для работы с лайками через БД
@Component
public class LikesDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // возвращение лайков фильма
    public List<Long> getFilmLikes(long id) {
        String sqlQuery = "select user_id from likes where film_id = ?";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, id).stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    // возвращение лайков пользователя
    public List<Long> getUserLikes(long id) {
        String sqlQuery = "select film_id from likes where user_id = ?";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, id).stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    // сохранение лайка
    public void saveLike(long filmId, long userId) {
        String sqlQuery = "merge into likes (user_id, film_id) key(user_id, film_id)" +
                "values (?,?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    // удаление лайка
    public void deleteLike(long filmId, long userId) {
        String sqlQuery = "delete from likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);

    }

}
