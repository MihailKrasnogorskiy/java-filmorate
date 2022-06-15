CREATE TABLE IF NOT EXISTS film (
    film_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar   NOT NULL,
    description varchar   NOT NULL,
    release_date date   NOT NULL,
    duration int   NOT NULL,
    rating_id int   NOT NULL,
    CONSTRAINT pk_film PRIMARY KEY (
        film_id
     )
);

CREATE TABLE IF NOT EXISTS users (
    email varchar   NOT NULL,
    user_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    login varchar   NOT NULL,
    name varchar   NOT NULL,
    birthday date   NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (
        user_id
     )
);

CREATE TABLE IF NOT EXISTS likes (
    user_id int   NOT NULL,
    film_id int   NOT NULL,
    CONSTRAINT pk_likes PRIMARY KEY (
        user_id
     )
);

CREATE TABLE IF NOT EXISTS ganre (
    ganre_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    ganre varchar   NOT NULL,
    CONSTRAINT pk_ganre PRIMARY KEY (
        ganre_id
     )
);

CREATE TABLE IF NOT EXISTS rating (
    rating_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    rating varchar   NOT NULL,
    CONSTRAINT pk_rating PRIMARY KEY (
        rating_id
     )
);

CREATE TABLE IF NOT EXISTS friends (
    user_id int   NOT NULL,
    friend int   NOT NULL,
    friendship_status int   NOT NULL,
    CONSTRAINT pk_friends PRIMARY KEY (
        user_id
     )
);

CREATE TABLE IF NOT EXISTS friendship_status (
    status_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    status varchar   NOT NULL,
    CONSTRAINT pk_friendship_status PRIMARY KEY (
        status_id
     )
);

CREATE TABLE IF NOT EXISTS film_genre (
    film_id int   NOT NULL,
    ganre_id int   NOT NULL,
    CONSTRAINT pk_film_genre PRIMARY KEY (
        film_id
     )
);

ALTER TABLE film ADD CONSTRAINT IF NOT EXISTS fk_film_rating_id FOREIGN KEY(rating_id)
REFERENCES rating (rating_id);

ALTER TABLE likes ADD CONSTRAINT IF NOT EXISTS fk_likes_user_id FOREIGN KEY(user_id)
REFERENCES users (user_id);

ALTER TABLE likes ADD CONSTRAINT IF NOT EXISTS fk_likes_film_id FOREIGN KEY(film_id)
REFERENCES film (film_id);

ALTER TABLE friends ADD CONSTRAINT IF NOT EXISTS fk_friends_user_id FOREIGN KEY(user_id)
REFERENCES users (user_id);

ALTER TABLE friends ADD CONSTRAINT IF NOT EXISTS fk_friends_friend FOREIGN KEY(friend)
REFERENCES users (user_id);

ALTER TABLE friends ADD CONSTRAINT IF NOT EXISTS fk_friends_friendship_status FOREIGN KEY(friendship_status)
REFERENCES friendship_status (status_id);

ALTER TABLE film_genre ADD CONSTRAINT IF NOT EXISTS fk_film_genre_film_id FOREIGN KEY(film_id)
REFERENCES film (film_id);

ALTER TABLE film_genre ADD CONSTRAINT IF NOT EXISTS fk_film_genre_ganre_id FOREIGN KEY(ganre_id)
REFERENCES ganre (ganre_id);