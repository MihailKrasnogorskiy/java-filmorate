package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.ValidationException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
//класс фильм
public class Film {
    private long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @Past
    private LocalDate releaseDate;
    private Duration duration;

    private Set<Long> likes = new HashSet<>();

    public Film(String name, String description, LocalDate releaseDate, Duration duration) {
        validation(releaseDate, duration);
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Set<Long> getLikes() {
        return likes;
    }

    //валидация даты и продолжительности
    void validation(LocalDate releaseDate, Duration duration) {
        final LocalDate firstFilmPresentation = LocalDate.of(1895, 12, 28);
        if (releaseDate.isBefore(firstFilmPresentation)) {
            throw new ValidationException("This date is before 12/28/1895");
        }
        if (duration.isNegative() || duration.isZero()) {
            throw new ValidationException("Duration is not positive");
        }
    }
}
