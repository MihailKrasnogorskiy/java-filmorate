package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.film.FilmStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@Validated
@Slf4j
//класс REST контроллера
public class FilmController {
    private FilmStorage storage;

    @Autowired
    public FilmController(FilmStorage storage) {
        this.storage = storage;
    }

    @GetMapping
    //возвращение списка фильмов
    public List<Film> findAll() {
        return storage.findAllFilms();
    }

    @PostMapping
    //создание фильма
    public Film create(@Valid @RequestBody Film film) {
        storage.createFilm(film);
        log.info("Create new film {}", film);
        return film;
    }

    @PutMapping
    //обновление фильма
    public Film update(@Valid @RequestBody Film film) {
        storage.updateFilm(film);
        log.info("Update film {}", film);
        return film;
    }
}
