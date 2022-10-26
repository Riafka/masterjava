DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS user_seq;
DROP TABLE IF EXISTS cities;
DROP SEQUENCE IF EXISTS cities_seq;
DROP TABLE IF EXISTS groups;
DROP SEQUENCE IF EXISTS group_seq;
DROP TABLE IF EXISTS projects;
DROP SEQUENCE IF EXISTS project_seq;
DROP TYPE IF EXISTS user_flag;
DROP TYPE IF EXISTS group_type;

CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');
CREATE TYPE group_type AS ENUM ('CURRENT','FINISHED');
CREATE SEQUENCE user_seq START 100000;
CREATE SEQUENCE cities_seq START 100000;
CREATE SEQUENCE project_seq START 100000;
CREATE SEQUENCE group_seq START 100000;

CREATE TABLE cities
(
    id         INTEGER PRIMARY KEY DEFAULT nextval('cities_seq'),
    short_name TEXT NOT NULL UNIQUE,
    full_name  TEXT NOT NULL
);

CREATE TABLE projects
(
    id          INTEGER PRIMARY KEY DEFAULT nextval('project_seq'),
    name        TEXT NOT NULL UNIQUE,
    description TEXT NOT NULL
);

CREATE TABLE groups
(
    id         INTEGER PRIMARY KEY DEFAULT nextval('group_seq'),
    name       TEXT       NOT NULL UNIQUE,
    type       group_type NOT NULL,
    project_id INTEGER,
    CONSTRAINT fk_project_id FOREIGN KEY (project_id) REFERENCES projects (id)
);

CREATE TABLE users
(
    id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
    full_name TEXT      NOT NULL,
    email     TEXT      NOT NULL,
    flag      user_flag NOT NULL,
    cities_id INTEGER,
    CONSTRAINT fk_cities_id FOREIGN KEY (cities_id) REFERENCES cities (id)
);

CREATE UNIQUE INDEX email_idx ON users (email);
