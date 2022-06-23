package ru.yandex.practicum.filmorate.model.users;

import lombok.Data;
//класс заявки в друзья
@Data
public class Request {
    private long id;
    private long incomingId;
    private long outgoingId;
    private String status;

    public Request(long outgoingId, long incomingId, String status) {
        this.incomingId = incomingId;
        this.outgoingId = outgoingId;
        this.status = status;
    }
}
