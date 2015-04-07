-- CS 122B Project #1 Spring 2015
-- Group #33
-- Robin Chen (95812659)
-- Valentin Yang (30062256)

CREATE TABLE movies (
	id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
	title VARCHAR(100) NOT NULL,
    year INT NOT NULL,
    director VARCHAR(100) NOT NULL,
    banner_url VARCHAR(200), 
    trailer_url VARCHAR(200)
) ENGINE=InnoDB;

CREATE TABLE stars (
	id INTEGER(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    dob DATE,
    photo_url VARCHAR(200)
) ENGINE=InnoDB;

CREATE TABLE genres (
	id INTEGER(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(32) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE creditcards (
	id VARCHAR(20) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    expiration DATE NOT NULL
) ENGINE=InnoDB;

CREATE TABLE stars_in_movies (
	star_id INTEGER(11) NOT NULL REFERENCES stars(id),
    movie_id INTEGER(11) NOT NULL REFERENCES movies(id)
) ENGINE=InnoDB;

CREATE TABLE genres_in_movies (
	genre_id INTEGER(11) NOT NULL REFERENCES genres(id),
    movie_id INTEGER(11) NOT NULL REFERENCES movies(id)
) ENGINE=InnoDB;

CREATE TABLE customers (
	id INTEGER(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    cc_id VARCHAR(20) NOT NULL REFERENCES creditcards(id),
    address VARCHAR(200) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(20) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE sales (
	id INTEGER(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    customer_id INTEGER NOT NULL REFERENCES customers(id),
    movie_id INT NOT NULL REFERENCES movies(id),
    sale_date DATE NOT NULL
) ENGINE=InnoDB;


