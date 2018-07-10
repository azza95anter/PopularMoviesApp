package com.example.android.popularmoviesapp;

/**
 * Created by azza anter on 3/13/2018.
 */

public class ReviewModel {
    private String id;
    private String content;
    private String author;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }
}
