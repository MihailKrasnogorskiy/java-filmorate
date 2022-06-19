package ru.yandex.practicum.filmorate.storage.film.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.films.mpa.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

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
        Mpa mpa = new Mpa(id);
        mpa.setName(ratingString);
        return mpa;
    }
}
