package ru.yandex.practicum.filmorate.storage.film.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.films.mpa.Mpa;
import ru.yandex.practicum.filmorate.model.films.mpa.enumMpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

@Component
public class MpaDao {
    private final JdbcTemplate jdbcTemplate;

    public MpaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa get(int id) {
        String sqlQuery = "select * from ratings where rating_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeMpa(id, rs), id);
    }

    private Mpa makeMpa(int id, ResultSet rs) throws SQLException {
        String ratingString = rs.getString("rating");
        enumMpa rating = Stream.of(enumMpa.values())
                .filter(s -> s.toString().equals(ratingString))
                .findFirst()
                .get();
        Mpa mpa = new Mpa(id);
        mpa.setName(rating);
        return mpa;
    }
}
