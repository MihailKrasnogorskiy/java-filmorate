package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.ValidationException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;


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

    public Film(String name, String description, LocalDate releaseDate, Duration duration) {
        validation(releaseDate, duration);
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
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
