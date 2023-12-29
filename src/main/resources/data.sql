INSERT INTO age_limits (limit_value) VALUES ('0+'), ('6+'), ('12+'), ('18+');

INSERT INTO movies (name, duration, release_date, age_limit_id, description)
    VALUES ('Оппенгеймер', 180, '2023-07-21', 1, 'Фильм об Оппенгеймере');

INSERT INTO movies (name, duration, release_date, age_limit_id, description)
    VALUES ('Барби', 120, '2023-07-21', 2, 'Фильм о Барби');

INSERT INTO movies (name, duration, release_date, age_limit_id, description)
    VALUES ('Голодные игры: Баллада о певчих птицах и змеях', 157, '2023-10-5', 3, '');



INSERT INTO genres (name) VALUES ('биография'), ('драма'), ('история');
INSERT INTO countries (short_name, full_name) VALUES ('США', 'Соединенные Штаты Америки'), ('Россия', 'Российская Федерация'), ('Германия', 'Германия');

INSERT INTO movies_genres (movie_id, genre_id) VALUES (1, 1), (1, 2), (1, 3);
INSERT INTO movies_countries (movie_id, country_id) VALUES (1, 1), (1, 2);

