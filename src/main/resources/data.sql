MERGE INTO ratings
    (rating_id, rating)
    KEY (rating_id)
    VALUES (1, 'G'),
    (2, 'PG'),
    (3, 'PG13'),
    (4, 'R'),
    (5, 'NC17');

MERGE INTO genres
    (genre_id, genre)
    KEY (genre_id)
    VALUES (1, 'Комедия'),
    (2, 'Драма'),
    (3, 'Мультфильм'),
    (4, 'Триллер'),
    (5, 'Документальный');

MERGE INTO friendship_status
    (status_id, status)
    KEY (status_id)
    VALUES (1, 'Дружба'),
    (2, 'Подписка');