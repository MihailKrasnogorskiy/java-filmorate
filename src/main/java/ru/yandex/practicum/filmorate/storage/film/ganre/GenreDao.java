package ru.yandex.practicum.filmorate.storage.film.ganre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.films.genre.Genre;
import ru.yandex.practicum.filmorate.model.films.genre.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

@Component
public class GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenreDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre get(int id) {
        String sqlQuery = "select * from genres where genre_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeMpa(id, rs), id);
    }

    private Genre makeMpa(int id, ResultSet rs) throws SQLException {
        String genreString = rs.getString("genre");
        Genres name = Stream.of(Genres.values())
                .filter(s -> s.toString().equals(genreString))
                .findFirst()
                .get();
        Genre genre = new Genre(id);
        genre.setName(name);
        return genre;
    }
}
