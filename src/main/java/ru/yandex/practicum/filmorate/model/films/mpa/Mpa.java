package ru.yandex.practicum.filmorate.model.films.mpa;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.Getter;

@Getter
public class Mpa {
    private final int id;
    private String name;

    public Mpa(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public Mpa(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
