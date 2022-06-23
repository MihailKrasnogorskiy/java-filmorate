package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FoundException;
import ru.yandex.practicum.filmorate.exceptions.IllegalHttpMethodException;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.model.films.genre.Genre;
import ru.yandex.practicum.filmorate.model.films.genre.Genres;
import ru.yandex.practicum.filmorate.services.IdCreator;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

@Component
@Slf4j
//класс реализация хранилища фильмов
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap<>();
    private final InMemoryUserStorage userStorage;
    private final IdCreator filmIdCreator = new IdCreator();

    private final MpaStorage mpaStorage;

    @Autowired
    public InMemoryFilmStorage(InMemoryUserStorage userStorage, MpaStorage mpaStorage) {
        this.userStorage = userStorage;
        this.mpaStorage = mpaStorage;
    }


    // создание фильма
    @Override
    public Film create(Film film) {
        if (film.getMpa().getId() < 0) {
            throw new FoundException("Unknown mpa");
        }
        film.setId(filmIdCreator.generateId());
        film.getMpa().setName(mpaStorage.getMpaNameById(film.getMpa().getId()));
        if (film.getGenres() != null) {

            createGenres(film);
        }
        films.put(film.getId(), film);
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
        film.getMpa().setName(mpaStorage.getMpaNameById(film.getMpa().getId()));
        if (film.getGenres() != null) {
            createGenres(film);
            if (film.getGenres().isEmpty()) {
                film.setGenres(null);
                films.put(film.getId(), film);
                System.out.println("genres был пустой " + films.get(film.getId()).getGenres());
                return new Film(film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(),
                        film.getDuration(), film.getMpa(), new ArrayList<>());
            }
        }
        films.put(film.getId(), film);
        log.info("Update film {}", film);
        return film;
    }

    //удаление фильма
    @Override
    public void delete(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FoundException("This film is not found");
        }
        films.remove(film.getId());
        log.info("Delete film {}", film);
    }

    //возвращение фильма по id
    @Override
    public Film getById(Long id) {
        if (!films.containsKey(id)) {
            throw new FoundException("This film is not found");
        }
        System.out.println("genres " + films.get(id).getGenres());
        return films.get(id);
    }

    // сохранение лайка
    @Override
    public void saveLike(long filmId, long userId) {
        userStorage.getById(userId).getLikedFilms().add(filmId);
        getById(filmId).getLikes().add(userId);
    }

    // удаление лайка
    @Override
    public void deleteLike(long filmId, long userId) {
        userStorage.getById(userId).getLikedFilms().remove(filmId);
        getById(filmId).getLikes().remove(userId);
    }

    //очистка хранилища
    public void clear() {
        films.clear();
        filmIdCreator.clear();
    }

    // создание жанра
    private void createGenres(Film film) {
        TreeSet<Integer> genreIds = new TreeSet<>();
        film.getGenres().stream()
                .map(Genre::getId)
                .forEach(genreIds::add);
        film.getGenres().clear();
        genreIds.stream()
                .forEach(s -> film.getGenres().add(new Genre(s)));

        for (Genre g : film.getGenres()) {
            if (g.getId() < 0 || g.getId() > 6) {
                throw new FoundException("Unknown genre");
            }
            switch (g.getId()) {
                case (1):
                    g.setName(Genres.Комедия);
                    break;
                case (2):
                    g.setName(Genres.Драма);
                    break;
                case (3):
                    g.setName(Genres.Мультфильм);
                    break;
                case (4):
                    g.setName(Genres.Триллер);
                    break;
                case (5):
                    g.setName(Genres.Документальный);
                    break;
                case (6):
                    g.setName(Genres.Боевик);
                    break;
            }
        }
    }
}
