package ru.yandex.practicum.filmorate.model.films;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.films.genre.Genre;
import ru.yandex.practicum.filmorate.model.films.mpa.Mpa;

import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
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
    @Min(1)
    private int duration;

    private Mpa mpa;

    private List<Genre> genres;

    private Set<Long> likes = new HashSet<>();

    public Film(long id, String name, String description, LocalDate releaseDate, int duration, Mpa mpa
            , List<Genre> genres) {
        validation(releaseDate);
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;

    }

    //возвращаем лайки
    public Set<Long> getLikes() {
        return likes;
    }

    //валидация даты
    void validation(LocalDate releaseDate) {
        final LocalDate firstFilmPresentation = LocalDate.of(1895, 12, 28);
        if (releaseDate.isBefore(firstFilmPresentation)) {
            throw new ValidationException("This date is before 12/28/1895");
        }
    }
}
