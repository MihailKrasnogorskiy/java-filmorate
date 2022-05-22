package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.servises.FilmService;
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

    @Autowired
    public FilmController(FilmStorage storage, FilmService service) {
        this.service = service;
    }

    @GetMapping
    //возвращение списка фильмов
    public List<Film> findAll() {
        return service.findAllFilms();
    }

    @PostMapping
    //создание фильма
    public Film create(@Valid @RequestBody Film film) {
        Film film1 = service.createFilm(film);
        log.info("Create new film {}", film);
        return film1;
    }

    @PutMapping
    //обновление фильма
    public Film update(@Valid @RequestBody Film film) {
        Film film1 = service.updateFilm(film);
        log.info("Update film {}", film);
        return film1;
    }

    //пользователь ставит лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        service.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void delleteLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        service.addLikeToFilm(id, userId);
    }

    //возвращает список из первых count фильмов по количеству лайков
    @GetMapping("/popular")

    public List<Film> findBestFilms(@RequestParam(defaultValue = "10") Integer count) {
        return service.getSortedFilms(count);
    }
}
