package ru.yandex.practicum.filmorate.storages.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.serveses.IDCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
//класс реализация хранилища фильмов
public class InMemoryFilmStorage implements FilmStorage {
    private IDCreator idCreator;
    private final HashMap<Long, Film> films = new HashMap<>();

    @Autowired
    public InMemoryFilmStorage(IDCreator idCreator) {
        this.idCreator = idCreator;
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(idCreator.generateId());
        films.put(film.getId(), film);
        log.info("Create new film {}", film);
        return film;
    }

    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new IllegalArgumentException("If you want to create film use HTTP method POST");
        }
        films.put(film.getId(), film);
        log.info("Update film {}", film);
        return film;
    }
}
