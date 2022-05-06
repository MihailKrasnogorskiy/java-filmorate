package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.ValidationException;
import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;


@Data
public class Film {
    @Positive
    private int id;
    @NotBlank
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @Past
    private LocalDate releaseDate;
    @NotBlank
    @NotNull
    @Positive
    private Duration duration;

    public Film(String name, String description, LocalDate releaseDate, Duration duration) {
        validation(releaseDate);
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    void validation(LocalDate releaseDate){
        final LocalDate firstFilmPresentation = LocalDate.of(1895,12,28);
        if(releaseDate.isBefore(firstFilmPresentation)){
            throw new ValidationException("This date is before 12/28/1895");
        }
    }
}
