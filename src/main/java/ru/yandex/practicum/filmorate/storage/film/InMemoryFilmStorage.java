package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FoundException;
import ru.yandex.practicum.filmorate.exceptions.IllegalHttpMethodException;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.servises.IdCreator;

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
    private final IdCreator filmIdCreator = new IdCreator();


    // создание фильма
    @Override
    public Film create(Film film) {
        film.setId(filmIdCreator.generateId());
        films.put(film.getId(), film);
        addToSortedSet(film.getId());
        log.info("Create new film {}", film);
        return film;
    }

    //возвращение всех фильмов
    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    //обновление фильма
    @Override
    public Film update(Film film) {
        if (film.getId() < 0) {
            throw new FoundException("This film not found");
        }
        if (!films.containsKey(film.getId())) {
            throw new IllegalHttpMethodException("If you want to create film use HTTP method POST");
        }
        films.put(film.getId(), film);
        addToSortedSet(film.getId());
        log.info("Update film {}", film);
        return film;
    }

    //удаление фильма
    @Override
    public void delete(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FoundException("This film is not found");
        }
        sortedFilms.remove(film.getId());
        films.remove(film.getId());
        log.info("Delete film {}", film);
    }

    //добавление фильмов в сортировку
    private void addToSortedSet(Long id) {
        sortedFilms.remove(id);
        sortedFilms.add(id);
    }

    //возвращение сортированных фильмов
    public List<Long> getSorted() {
        return new ArrayList<>(sortedFilms);
    }

    //возвращение фильма по id
    @Override
    public Film getById(Long id) {
        if (!films.containsKey(id)) {
            throw new FoundException("This film is not found");
        }
        return films.get(id);
    }

    //очистка хранилища
    public void clear() {
        films.clear();
        sortedFilms.clear();
        filmIdCreator.clear();
    }
}
