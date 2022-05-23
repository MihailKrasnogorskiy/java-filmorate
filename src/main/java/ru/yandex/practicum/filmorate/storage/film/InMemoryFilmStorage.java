package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FoundException;
import ru.yandex.practicum.filmorate.exceptions.IllegalHttpMethodException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.servises.film.FilmIdCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

@Component
@Slf4j
//класс реализация хранилища фильмов
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap<>();
    private final TreeSet<Long> sortedFilms = new TreeSet<>((id1, id2) -> {
        if (films.get(id2).getLikes().size() == films.get(id1).getLikes().size())
            return (int) (id2 - id1);
        return films.get(id2).getLikes().size() - films.get(id1).getLikes().size();
    });
    private final FilmIdCreator filmIdCreator;

    @Autowired
    public InMemoryFilmStorage(FilmIdCreator filmIdCreator) {
        this.filmIdCreator = filmIdCreator;
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(filmIdCreator.generateId());
        films.put(film.getId(), film);
        addFilmToSortedFilmSet(film.getId());
        log.info("Create new film {}", film);
        return film;
    }

    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film updateFilm(Film film) {
        if (film.getId() < 0) {
            throw new FoundException("This film not found");
        }
        if (!films.containsKey(film.getId())) {
            throw new IllegalHttpMethodException("If you want to create film use HTTP method POST");
        }
        films.put(film.getId(), film);
        addFilmToSortedFilmSet(film.getId());
        log.info("Update film {}", film);
        return film;
    }

    @Override
    public void deleteFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FoundException("This film is not found");
        }
        sortedFilms.remove(film.getId());
        films.remove(film.getId());
        log.info("Delete film {}", film);
    }

    private void addFilmToSortedFilmSet(Long id) {
        sortedFilms.remove(id);
        sortedFilms.add(id);
    }

    public List<Long> getSortedFilms() {
        return new ArrayList<>(sortedFilms);
    }

    @Override
    public Film getFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new FoundException("This film is not found");
        }
        return films.get(id);
    }

    public void clear() {
        films.clear();
        sortedFilms.clear();
        filmIdCreator.clear();
    }
}
