package com.example.android.popularmoviesapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by azza anter on 3/2/2018.
 */

public class FavoriteDbHelper extends SQLiteOpenHelper {
    public static final String name = "Movie.db";
    public static final int version = 1;
    public Context context;

    public FavoriteDbHelper(Context context) {
        super(context, name, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES_TABLE =
                "CREATE TABLE " + FavoriteContract.MovieEntry.TABLE_NAME + " (" +
                        FavoriteContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoriteContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        FavoriteContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                        FavoriteContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                        FavoriteContract.MovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL, " +
                        FavoriteContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                        FavoriteContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL" +
                        " );";
        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean DataSearch(Integer movieID) {

        Cursor cursor = getReadableDatabase().rawQuery("select id from movies where id =" + movieID + "", null);

        if (cursor == null || !cursor.moveToNext()) {
            return false;
        }
        cursor.close();
        return true;

    }
}
