package ru.yandex.practicum.filmorate.services.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FoundException;
import ru.yandex.practicum.filmorate.model.films.mpa.Mpa;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaDao;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaStorage;

import java.util.List;

//сервис возрастных рейтингов
@Component
public class MpaService {

    private final MpaStorage storage;

    @Autowired
    public MpaService(MpaDao storage) {
        this.storage = storage;
    }

    // возвращаем все рейтинги
    public List<Mpa> getAll() {
        return storage.getAll();
    }

    // возвращаем рейтинг по id
    public Mpa getById(int id) {
        if (id < 1 || id > 5) {
            throw new FoundException("Unknown mpa");
        }
        return storage.get(id);
    }
}
