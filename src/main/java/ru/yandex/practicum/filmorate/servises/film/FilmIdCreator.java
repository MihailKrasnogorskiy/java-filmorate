package ru.yandex.practicum.filmorate.servises.film;

import org.springframework.stereotype.Service;

@Service
//класс для генерации ID
public class FilmIdCreator {
    private long id = 1;

    //генерация ID
    public long generateId() {
        return id++;
    }

    public void clear() {
        id = 1;
    }
}
