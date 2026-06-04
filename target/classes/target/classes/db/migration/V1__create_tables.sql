CREATE TABLE IF NOT EXISTS genre (
    id   SERIAL      PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS platform (
    id   SERIAL      PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS game (
    id           SERIAL       PRIMARY KEY,
    title        VARCHAR(150) NOT NULL,
    release_year INT,
    description  TEXT,
    genre_id     INT          NOT NULL,
    CONSTRAINT fk_game_genre FOREIGN KEY (genre_id) REFERENCES genre (id)
);

CREATE TABLE IF NOT EXISTS game_platform (
    game_id     INT NOT NULL,
    platform_id INT NOT NULL,
    CONSTRAINT pk_game_platform   PRIMARY KEY (game_id, platform_id),
    CONSTRAINT fk_gp_game         FOREIGN KEY (game_id)     REFERENCES game (id)     ON DELETE CASCADE,
    CONSTRAINT fk_gp_platform     FOREIGN KEY (platform_id) REFERENCES platform (id)
);
