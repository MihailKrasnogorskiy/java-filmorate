package ru.yandex.practicum.filmorate.storage.film.ganre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.model.films.genre.Genre;
import ru.yandex.practicum.filmorate.model.films.genre.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//класс для работы с жанрами в БД
@Component
public class GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //возврашаем жанр по id
    public Genre get(int id) {
        String sqlQuery = "select * from genres where genre_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeGenre(id, rs), id);
    }

    // создание жанра
    private Genre makeGenre(int id, ResultSet rs) throws SQLException {
        String genreString = rs.getString("genre");
        Genres name = Stream.of(Genres.values())
                .filter(s -> s.toString().equals(genreString))
                .findFirst()
                .get();
        Genre genre = new Genre(id);
        genre.setName(name);
        return genre;
    }

    // возвращаем жанры для фильма
    public List<Genre> getFilmGenres(long filmId) {
        String sqlQuery = "select genre_id from film_genres where film_id = ?";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, filmId).stream()
                .map(this::get)
                .collect(Collectors.toList());
    }

    // возвращаем список жанров
    public List<Genre> getAll() {
        String sqlQuery = "select genre_id from genres";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class).stream()
                .map(this::get)
                .collect(Collectors.toList());
    }

    // сохраняем список жанров фильма
    public void saveFilmGenres(List<Genre> genres, long filmId) {
        String sqlQuery = "merge into film_genres (film_id, genre_id) key(film_id, genre_id) " +
                "values (?, ?)";
        genres.forEach(s -> jdbcTemplate.update(sqlQuery, filmId, s.getId()));
    }

    // обновляем список жанров фильма
    public void updateFilmsGenres(Film film) {
        String sqlQuery = "delete from film_genres where film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        if (!film.getGenres().isEmpty()) {
            saveFilmGenres(film.getGenres(), film.getId());
        }
    }
}
