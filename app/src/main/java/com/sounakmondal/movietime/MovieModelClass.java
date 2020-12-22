package com.sounakmondal.movietime;

public class MovieModelClass {
    String id,name, img, overview, backdrop, rating, originalLanguage, releaseDate ;


    //constructors
    public MovieModelClass(String id, String name, String img, String overview, String backdrop, String rating, String originalLanguage, String releaseDate) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.overview = overview;
        this.backdrop = backdrop;
        this.rating = rating;
        this.originalLanguage = originalLanguage;
        this.releaseDate = releaseDate;
    }

    public MovieModelClass() {
    }

    //getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
