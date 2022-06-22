package ru.yandex.practicum.filmorate.storage.film.mpa;

import ru.yandex.practicum.filmorate.model.films.mpa.Mpa;

import java.util.List;

public interface MpaStorage {
    Mpa get(int id);

    List<Mpa> getAll();

    String getMpaNameById(int id);
}
