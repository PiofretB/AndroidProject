package com.example.android.testpopularmovies.models;

import java.io.Serializable;

public class Cast implements Serializable {
    private String name;
    private String role;
    private String img_link;
    private String birthday;
    private int id;
    private String biography;
    private String birth_place;
    private String deathday;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImg_link() {
        return "https://image.tmdb.org/t/p/w500" + img_link;
    }

    public void setImg_link(String img_link) {
        this.img_link = img_link;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBirth_place() {
        return birth_place;
    }

    public void setBirth_place(String birth_place) {
        this.birth_place = birth_place;
    }

    public String getDeathday() {
        return deathday;
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }
}