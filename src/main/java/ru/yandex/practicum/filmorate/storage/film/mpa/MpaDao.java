package ru.yandex.practicum.filmorate.storage.film.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.films.mpa.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
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

    public List<Mpa> getAll() {
        String sqlQuery = "select rating_id from ratings";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class).stream()
                .map(this::get)
                .collect(Collectors.toList());
    }
}
