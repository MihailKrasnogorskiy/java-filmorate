package ru.yandex.practicum.filmorate.storage.film.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.films.mpa.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

// класс для работы с рейтингами через БД
@Component
@Primary
public class MpaDao implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // возвращаем рейтинг по ид
    @Override
    public Mpa get(int id) {
        String sqlQuery = "select * from ratings where rating_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeMpa(id, rs), id);
    }

    // создание объекта рейтинга
    private Mpa makeMpa(int id, ResultSet rs) throws SQLException {
        String ratingString = rs.getString("rating");
        Mpa mpa = new Mpa(id);
        mpa.setName(ratingString);
        return mpa;
    }

    // возвращение списка рейтингов
    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "select rating_id from ratings";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class).stream()
                .map(this::get)
                .collect(Collectors.toList());
    }

    // возвращение имени рейтинга по id
    @Override
    public String getMpaNameById(int id) {
        String sqlQuery = "select rating from ratings where rating_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, String.class, id);
    }

}
