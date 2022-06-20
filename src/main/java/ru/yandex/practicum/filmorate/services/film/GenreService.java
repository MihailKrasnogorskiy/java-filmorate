package ru.yandex.practicum.filmorate.services.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FoundException;
import ru.yandex.practicum.filmorate.model.films.genre.Genre;
import ru.yandex.practicum.filmorate.model.films.genre.Genres;
import ru.yandex.practicum.filmorate.storage.film.ganre.GenreDao;

import java.util.List;

@Component
public class GenreService {

    private final GenreDao storage;

    @Autowired
    public GenreService(GenreDao genreDao) {
        this.storage = genreDao;
    }

    public List<Genre> getAll() {
        return storage.getAll();
    }

    public Genre getById(int id) {
        if (id < 1 || id > Genres.values().length) {
            throw new FoundException("Unknown genre");
        }
        return storage.get(id);
    }
}
