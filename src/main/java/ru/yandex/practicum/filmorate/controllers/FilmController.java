package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

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
    private final HashMap<Long, Film> films = new HashMap<>();
    private long id = 1;

    @GetMapping
    //возвращение списка фильмов
    public List<Film> findAll() {
        log.trace("Количестов пользователей: {}", films.entrySet().size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    //создание фильма
    public Film create(@Valid @RequestBody Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Create new film {}", film);
        return film;
    }

    @PutMapping
    //обновление фильма
    public Film update(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            throw new IllegalArgumentException("If you want to create film use HTTP method POST");
        }
        films.put(film.getId(), film);
        log.info("Update film {}", film);
        return film;
    }

    // создание id
    private long generateId() {
        return id++;
    }
}
