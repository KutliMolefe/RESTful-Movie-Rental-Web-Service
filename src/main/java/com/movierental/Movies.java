package com.movierental;
public class Movies {
    private int id;
    private String title;
    private String genre;
    private byte[] image;

    

    public Movies(int id, String title, String genre, byte[] image) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public byte[] getImage() {
        return image;
    }
    
}