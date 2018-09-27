package com.projects.darknigh.mydbapp;

import java.util.List;

public interface IDatabaseHandler {
    void addFilm(Film film);
    Film getFilm(int id);
    List<Film> getAllFilms();
    void updateFilm(Film film);
    void deleteFilm(Film film);
    void deleteAll();
}
