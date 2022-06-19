package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LikesDao {
    private final JdbcTemplate jdbcTemplate;

    public LikesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Long> getFilmLikes(long id) {
        String sqlQuery = "select user_id from likes where film_id = ?";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, id).stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    public List<Long> getUserLikes(long id) {
        String sqlQuery = "select film_id from likes where user_id = ?";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, id).stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    public void saveLike(long filmId, long userId) {
        String sqlQuery = "merge into likes (user_id, film_id) key(user_id, film_id)" +
                "values (?,?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    public void deleteLike(long filmId, long userId) {
        String sqlQuery = "delete from likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);

    }

}
