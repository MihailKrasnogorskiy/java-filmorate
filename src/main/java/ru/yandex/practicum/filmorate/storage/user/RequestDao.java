package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.users.Request;

import java.sql.ResultSet;
import java.sql.SQLException;

// класс для работы с заявками через БД
@Component
public class RequestDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RequestDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // возвращение заявки по id
    public Request get(int id) {
        String sqlQuery = "select * from requests where request_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeRequest(id, rs), id);
    }

    // возвращение id заявки
    public int getRequestId(long incomingId, long outgoingId) {
        String sqlQuery = "select request_id from requests where outgoing_id = ? and incoming_id = ? ";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class, outgoingId, incomingId);
    }

    // создание объекта заявки
    private Request makeRequest(int id, ResultSet rs) throws SQLException {
        int requestId = rs.getInt("request_id");
        int outgoingId = rs.getInt("outgoing_id");
        int incomingId = rs.getInt("incoming_id");
        String status = rs.getString("status");
        Request request = new Request(outgoingId, incomingId, status);
        request.setId(requestId);
        return request;
    }

    // создание заявки
    public Request create(Request request) {
        String sqlQuery = "insert into requests (outgoing_id, incoming_id, status) " +
                "values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                request.getOutgoingId(),
                request.getIncomingId(),
                request.getStatus()
        );

        sqlQuery = "select request_id from requests order by request_id desc limit 1 ";
        int requestId = jdbcTemplate.queryForObject(sqlQuery, Integer.class);
        return get(requestId);
    }

    // обновление заявки
    public Request update(Request request) {
        String sqlQuery = "update requests set outgoing_id = ?, incoming_id = ?, status = ?" +
                "where request_id = ?";
        jdbcTemplate.update(sqlQuery,
                request.getOutgoingId(),
                request.getIncomingId(),
                request.getStatus(),
                request.getId()
        );

        sqlQuery = "select request_id from requests order by request_id desc limit 1 ";
        int requestId = jdbcTemplate.queryForObject(sqlQuery, Integer.class);
        return get(requestId);
    }
}
