CREATE TABLE IF NOT EXISTS GENRE (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS MPA (
   id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   name varchar NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS USERS (
 id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
 name varchar DEFAULT 'unknown',
 login varchar NOT NULL,
 email varchar NOT NULL,
 birthdate date,
 CONSTRAINT birth CHECK (birthdate < CURRENT_DATE)
);

CREATE TABLE IF NOT EXISTS DIRECTORS (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS FILMS (
   id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   title varchar NOT NULL,
   description varchar(200) NOT NULL,
   release_date date,
   duration INTEGER,
   mpa_rate INTEGER REFERENCES mpa(id) ON DELETE CASCADE,
   CONSTRAINT films_constraint CHECK (duration > 0 AND release_date > '1895-12-28')
);

CREATE TABLE IF NOT EXISTS FILMS_GENRE (
 film_id INTEGER REFERENCES films(id) ON DELETE CASCADE,
 genre_id INTEGER REFERENCES genre(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS LIKES (
   film_id INTEGER REFERENCES films(id) ON DELETE CASCADE,
   user_id INTEGER REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS FRIENDS (
   user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
   friend_id INTEGER,
   status varchar
);

CREATE TABLE IF NOT EXISTS FEED (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    timestamp BIGINT,
    user_id INTEGER,
    event_type VARCHAR,
    operation VARCHAR,
    entity_id INTEGER
);

CREATE TABLE IF NOT EXISTS REVIEWS (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    review_type VARCHAR,
    user_id INTEGER REFERENCES users(id),
    film_id INTEGER REFERENCES films(id),
    review_content VARCHAR,
    useful_rate INTEGER
);

CREATE TABLE IF NOT EXISTS FILMS_DIRECTORS (
    film_id INTEGER REFERENCES films(id) ON DELETE CASCADE,
    director_id INTEGER REFERENCES DIRECTORS(id) ON DELETE CASCADE
);

