package ru.yandex.practicum.filmorate.serveses;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public class FilmService {

    public void addLikeToFilm(Film film, User user) {
        if (film != null && user != null) {
            film.getLikes().add(user.getId());
            user.getLikedFilms().add(film.getId());
        } else {
            throw new RuntimeException(); //todo change exeption
        }
    }

    public void deleteLikeToFilm(Film film,User user){
        if (film != null && user != null) {
            film.getLikes().remove(user.getId());
            user.getLikedFilms().remove(film.getId());
        } else {
            throw new RuntimeException(); //todo change exeption
        }
    }
}
