package ru.yandex.practicum.filmorate.storage.film.mpa;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FoundException;
import ru.yandex.practicum.filmorate.model.films.mpa.Mpa;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Component
@Primary
public class InMemoryMpaStorage implements MpaStorage {

    private final Mpa[] mpas = new Mpa[]{new Mpa(1, "G"), new Mpa(2, "PG"),
            new Mpa(3, "PG-13"), new Mpa(4, "R"), new Mpa(5, "NC-17")};

    @Override
    public Mpa get(int id) {
        if(id<0 || id> mpas.length){
            throw new FoundException("Unknown mpa");
        }
        return mpas[id - 1];
    }

    @Override
    public List<Mpa> getAll() {
        return Arrays.stream(mpas).collect(Collectors.toList());
    }
    @Override
    public String getMpaNameById(int id){
        return mpas[id - 1].getName();
    }
}
