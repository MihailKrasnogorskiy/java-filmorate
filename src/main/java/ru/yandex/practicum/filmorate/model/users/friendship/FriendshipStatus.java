package ru.yandex.practicum.filmorate.model.users.friendship;

import lombok.Data;

@Data
public class FriendshipStatus {
    private final int id;
    private final FriendshipStatuses status;
}
