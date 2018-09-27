package com.projects.darknigh.mydbapp;

import com.google.gson.annotations.SerializedName;

public class Film {

    @SerializedName("id")
    private int id;
    @SerializedName("filmName")
    private String name;
    @SerializedName("directorName")
    private String director;
    @SerializedName("budget")
    private int budget;
    @SerializedName("releaseDate")
    private String date;

    public Film(){}

    public Film(String name, String director, int budget, String date) {
        this.name = name;
        this.director = director;
        this.budget = budget;
        this.date = date;
    }

    public Film(int id, String name, String director, int budget, String date) {
        this.id = id;
        this.name = name;
        this.director = director;
        this.budget = budget;
        this.date = date;
    }

    public int getID() {
        return id;
    }

    public void setID(int id){ this.id = id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
