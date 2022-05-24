package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    //добавление фильма
    Film createFilm(Film film);

    //поиск всех фильмов
    List<Film> findAllFilms();

    //обновление фильма
    Film updateFilm(Film film);

    //удаление фильма
    void deleteFilm(Film film);

    //возвращение сортрованного списка фильмов
    List<Long> getSortedFilms();

    //возвращение фильма по id
    Film getFilmById(Long id);

}
