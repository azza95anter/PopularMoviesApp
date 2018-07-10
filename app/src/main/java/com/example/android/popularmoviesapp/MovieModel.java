package com.example.android.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by azza anter on 2/1/2018.
 */

/*
MovieModel is a class for five variables (Title,image,overview,rating,and the data) for each movie
in addition to their setter and getter methods for these variables
 */
public class MovieModel implements Parcelable {
    public Integer ID;
    public String title;
    public String posterPath;
    public String overview;
    public String voteAverage;
    public String releaseDate;
    private ArrayList<String> trailerList;

    // Important for initializing
    public MovieModel() {

    }

    public MovieModel(Integer ID, String title, String posterPath, String overview, String voteAverage, String releaseDate) {
        this.ID = ID;

        this.title = title;

        this.posterPath = posterPath;

        this.overview = overview;

        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }


    private ArrayList<String> reviewList;



    // setter methods
    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getID() {
        return ID;
    }

    public String getPosterPath() {
        /*
        here is the three parts of the image (Base URL, image size(w185), and concatenation  with poster path
         */

        return  posterPath;
    }

    // getter methods (return the value of variables)
    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(posterPath);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(voteAverage);
    }

    private MovieModel(Parcel in) {
        ID = in.readInt();
        posterPath = in.readString();
        title = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readString();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };
}