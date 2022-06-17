package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.model.films.mpa.Mpa;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class FilmDao {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;

    public FilmDao(JdbcTemplate jdbcTemplate, MpaDao mpaDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDao = mpaDao;
    }

    public Film create(Film film) {
        String sqlQuery = "insert into films (name, description, release_date, duration, rating_id) " +
                "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        sqlQuery = "select film_id from films order by film_id desc limit 1 ";
        long filmId = jdbcTemplate.queryForObject(sqlQuery, Integer.class);
        return getById(filmId);
    }

    public Film getById(Long id) {
        String sqlQuery = "select * from films where film_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeFilm(id, rs), id);
    }

    private Film makeFilm(Long id, ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        int ratingId = rs.getInt("rating_id");
        Mpa mpa = mpaDao.get(ratingId);
        return new Film(id, name, description, releaseDate, duration, mpa);
    }
}
