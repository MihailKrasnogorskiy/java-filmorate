package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.films.Film;

import java.util.List;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmDao filmDao;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmDao filmDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmDao = filmDao;
    }


    @Override
    public Film create(Film film) {
        return filmDao.create(film);
    }

    @Override
    public List<Film> findAll() {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public void delete(Film film) {

    }

    @Override
    public List<Long> getSorted() {
        return null;
    }

    @Override
    public Film getById(Long id) {
        return filmDao.getById(id);
    }

}
