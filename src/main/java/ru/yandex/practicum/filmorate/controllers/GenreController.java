package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.films.genre.Genre;
import ru.yandex.practicum.filmorate.services.film.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService service;

    @Autowired
    public GenreController(GenreService genreService) {
        this.service = genreService;
    }

    @GetMapping
    //возвращение списка жанров
    public List<Genre> getAll() {
        return service.getAll();
    }

    //возвращаем жанр по id
    @GetMapping("/{id}")
    public Genre findFilmById(@PathVariable int id) {
        return service.getById(id);
    }
}
