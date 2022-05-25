package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.servises.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Validated
@Slf4j
//класс REST контроллера
public class FilmController {
    private final FilmService service;
    private final FilmStorage storage;

    @Autowired
    public FilmController(FilmService service, FilmStorage storage) {
        this.service = service;
        this.storage = storage;
    }

    @GetMapping
    //возвращение списка фильмов
    public List<Film> findAll() {
        return storage.findAll();
    }

    @PostMapping
    //создание фильма
    public Film create(@Valid @RequestBody Film film) {
        return storage.create(film);
    }

    @PutMapping
    //обновление фильма
    public Film update(@Valid @RequestBody Film film) {
        return storage.update(film);
    }

    //пользователь ставит лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        service.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        service.deleteLikeToFilm(id, userId);
    }

    //возвращает список из первых count фильмов по количеству лайков, значение по умолчанию 10
    @GetMapping("/popular")

    public List<Film> findBestFilms(@RequestParam(defaultValue = "10") Integer count) {
        return service.getSortedFilms(count);
    }

    //возвращаем фильм по id
    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable Long id) {
        return storage.getById(id);
    }
}