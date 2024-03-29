package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.films.Film;

import java.util.List;

public interface FilmStorage {
    //добавление фильма
    Film create(Film film);

    //поиск всех фильмов
    List<Film> findAll();

    //обновление фильма
    Film update(Film film);

    //удаление фильма
    void delete(Film film);

    //возвращение фильма по id
    Film getById(Long id);

    //сохранение лайка
    void saveLike(long filmId, long userId);

    //удаление лайка
    void deleteLike(long filmId, long userId);
}
