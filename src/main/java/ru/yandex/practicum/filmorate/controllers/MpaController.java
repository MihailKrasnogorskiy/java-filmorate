package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.films.mpa.Mpa;
import ru.yandex.practicum.filmorate.services.film.MpaService;

import java.util.List;

//контроллер возрастных рейтингов
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService service;

    @Autowired
    public MpaController(MpaService service) {
        this.service = service;
    }

    @GetMapping
    //возвращение списка возрастных рейтингов
    public List<Mpa> getAll() {
        return service.getAll();
    }

    //возвращаем возрастной рейтинг по id
    @GetMapping("/{id}")
    public Mpa findMpaById(@PathVariable int id) {
        return service.getById(id);
    }
}
