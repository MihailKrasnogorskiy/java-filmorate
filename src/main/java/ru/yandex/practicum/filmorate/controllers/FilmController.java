package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private HashMap<Integer, Film> films = new HashMap<>();
    private int id = 1;

    Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public List<Film> findAll() {
        log.trace("Количестов пользователей: {}", films.entrySet().size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.trace(film.toString());
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        films.put(film.getId(), film);
        return film;
    }

    private int generateId(){
        return id++;
    }
}
