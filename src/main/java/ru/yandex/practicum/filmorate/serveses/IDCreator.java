package ru.yandex.practicum.filmorate.serveses;

import org.springframework.stereotype.Service;

@Service
//класс для генерации ID
public class IDCreator {
    private long id = 1;

    //генерация ID
    public long generateId() {
        return id++;
    }
}
