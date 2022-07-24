package ru.yandex.practicum.filmorate.services;


//класс для генерации ID
public class IdCreator {
    private long id = 1;

    //генерация ID
    public long generateId() {
        return id++;
    }

    //сброс счётчика
    public void clear() {
        id = 1;
    }
}
