package ru.yandex.practicum.filmorate.servises.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

//сервис класс для обработки бизнеслогики связанной с фильмами
@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage storage, UserStorage userStorage) {
        this.filmStorage = storage;
        this.userStorage = userStorage;
    }

    //добавление лайков
    public void addLikeToFilm(Long filmId, Long userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        film.getLikes().add(userId);
        user.getLikedFilms().add(filmId);
        log.info("Film  id : {} liked user id : {}", filmId, userId);
    }

    //удаление лайков
    public void deleteLikeToFilm(Long filmId, Long userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        film.getLikes().remove(userId);
        user.getLikedFilms().remove(filmId);
        log.info("User  id : {} delete like to film id : {}", userId, filmId);
    }

    //возвращение популярных фильмов
    public List<Film> getSortedFilms(Integer size) {
        if (size < 0) {
            throw new ValidationException("The number of films must be positive");
        }
        return filmStorage.getSorted().stream()
                .map(filmStorage::getById)
                .limit(size)
                .collect(Collectors.toList());
    }
}
