package ru.yandex.practicum.filmorate.storages.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorage {
        //добавление фильма
        public Film createFilm(Film film);
        //поиск всех фильмов
        public List<Film> findAllFilms();
        //обновление фильма
        public Film updateFilm(Film film);


}
