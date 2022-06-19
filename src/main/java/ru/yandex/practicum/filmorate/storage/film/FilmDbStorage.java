package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.model.films.genre.Genre;
import ru.yandex.practicum.filmorate.model.films.mpa.Mpa;
import ru.yandex.practicum.filmorate.storage.LikesDao;
import ru.yandex.practicum.filmorate.storage.film.ganre.GenreDao;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;
    private final LikesDao likesDao;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaDao mpaDao, GenreDao genreDao, LikesDao likesDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDao = mpaDao;
        this.genreDao = genreDao;
        this.likesDao = likesDao;
    }

    @Override
    public Film create(Film film) {
        if (film.getMpa() == null) {
            throw new ValidationException("MPA can't be null");
        }
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
        if (film.getGenres() != null) {
            genreDao.saveFilmGenres(film.getGenres(), filmId);
        }
        return getById(filmId);
    }

    @Override
    public Film update(Film film) {
        idValidation(film.getId());
        if (film.getMpa() == null) {
            throw new ValidationException("MPA can't be null");
        }
        String sqlQuery = "update films set name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? " +
                "where film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (film.getGenres() != null) {
            genreDao.updateFilmsGenres(film);
            if (film.getGenres().isEmpty()) {
                film = getById(film.getId());
                film.setGenres(new ArrayList<Genre>());
                return film;
            }
        }
        return getById(film.getId());
    }

    @Override
    public void delete(Film film) {
        idValidation(film.getId());
        String sqlQuery = "delete from films where film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "select * from films";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));

    }

    @Override
    public Film getById(Long id) {
        idValidation(id);
        String sqlQuery = "select * from films where film_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeFilm(rs), id);
    }

    @Override
    public void saveLike(long filmId, long userId) {
        likesDao.saveLike(filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        likesDao.deleteLike(filmId, userId);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        int ratingId = rs.getInt("rating_id");
        Mpa mpa = mpaDao.get(ratingId);
        List<Genre> genres = new ArrayList<>();
        if (!genreDao.getFilmGenres(id).isEmpty()) {
            genres.addAll(genreDao.getFilmGenres(id));
        } else {
            genres = null;
        }
        Film film = new Film(id, name, description, releaseDate, duration, mpa, genres);
        film.getLikes().addAll(likesDao.getFilmLikes(id));

        return film;
    }

    private void idValidation(long id) {
        List<Integer> filmIds;
        String sqlQuery = "select film_id from films";
        filmIds = jdbcTemplate.queryForList(sqlQuery, Integer.class);
        if (!filmIds.contains((int) id)) {
            throw new FoundException("Unknown film");
        }
    }
}
