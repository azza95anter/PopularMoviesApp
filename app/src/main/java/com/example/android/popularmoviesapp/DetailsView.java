package com.example.android.popularmoviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesapp.Adapter.ReviewAdapter;
import com.example.android.popularmoviesapp.Adapter.TrailerAdapter;
import com.example.android.popularmoviesapp.data.FavoriteContract;
import com.example.android.popularmoviesapp.data.FavoriteDbHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class DetailsView extends AppCompatActivity {
    final String Base_URL = "https://image.tmdb.org/t/p/w185";
    private static final String TAG = "ListViewReview";
    public static final String Result = "results";
    public static final String IIID = "id";
    public static final String Author = "author";
    public static final String Content = "content";
    public static final String Key = "key";
    public static final String Name = "name";
    boolean Exist;
    ImageView poster;
    FloatingActionButton favorite, trailers;
    ListView ListViewReviews;
    Toolbar toolbar;
    MovieModel movieModel;
    TrailersModel trailersModel;
    ReviewAdapter reviewAdapter;
    TrailerAdapter trailerAdapter;
    FavoriteDbHelper favoriteDbHelper;
    TextView textView, title, Desc, rate, date;
    ArrayList<ReviewModel> reviewsList = new ArrayList<ReviewModel>();
    ArrayList<TrailersModel> trailersList = new ArrayList<TrailersModel>();
    ListView ListViewTrailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);
        ListViewReviews = (ListView) findViewById(R.id.LV_Reviews);
        poster = (ImageView) findViewById(R.id.iv_Details);
        toolbar = (Toolbar) findViewById(R.id.tb_Details);
        favorite = (FloatingActionButton) findViewById(R.id.bt_fav);
        ListViewTrailers = (ListView) findViewById(R.id.gv_Trailers);
        textView = (TextView) findViewById(R.id.id);
        title = (TextView) findViewById(R.id.Title);
        Desc = (TextView) findViewById(R.id.Desc);
        rate = (TextView) findViewById(R.id.rateing);
        date = (TextView) findViewById(R.id.Date);
        favoriteDbHelper = new FavoriteDbHelper(this);


        movieModel = (MovieModel) getIntent().getParcelableExtra("MovieModel");

        String url = "http://api.themoviedb.org/3/movie/" + movieModel.getID() + "/reviews?api_key=d643f9d3c6ef8fc8c7aa952ba183c80a";
        String urlTrailers = "https://api.themoviedb.org/3/movie/" + movieModel.getID() + "/videos?api_key=d643f9d3c6ef8fc8c7aa952ba183c80a";
        new ReviewsTask().execute(url);
        new TrailersTask().execute(urlTrailers);

        ///
        trailersModel = (TrailersModel) getIntent().getParcelableExtra("VEDIO_ID");
        Intent lanuch = getIntent();
        if (lanuch.hasExtra("VEDIO_ID")) {
            //    startActivity(this,);
            Toast.makeText(getApplication(), "Done", Toast.LENGTH_SHORT).show();
        }
        // Explicit Intent to receive details of movie and display it on the DetailsView Activity
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("MovieModel")) {

            textView.setText("MovieID: " + movieModel.getID().toString());
            Picasso.with(getApplicationContext()).load(Base_URL + movieModel.getPosterPath()).into(poster);
            title.setText("MovieTitle: " + movieModel.getTitle());
            Desc.setText("Description:\n " + movieModel.getOverview());
            rate.setText("User Rating:  " + movieModel.getVoteAverage());
            date.setText("Release Date: " + movieModel.getReleaseDate());

            // this toolbar to display the Title of each movie
            toolbar.setTitle(movieModel.getTitle());

        } else {
            // if  something  wrong occurred , this toast will appear
            Toast.makeText(this, "API not found", LENGTH_SHORT).show();
        }
        Exist = favoriteDbHelper.DataSearch(movieModel.getID());
        if (Exist == true) {
            favorite.setImageResource(R.drawable.faaav);
        }

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Exist == true) {
                    //   favor.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    int deleted = getContentResolver().delete(FavoriteContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movieModel.getID())).build(), null, null);
                    if (deleted > 0) {
                        Toast.makeText(getApplication(), "Deleted from Favorites", Toast.LENGTH_SHORT).show();
                        favorite.setImageResource(R.drawable.sttar);
                    } else {
                        Toast.makeText(getApplication(), "Not Deleted from favorites", Toast.LENGTH_SHORT).show();
                    }

                } else if (Exist == false) {
                    favorite.setImageResource(R.drawable.faaav);
                    // onClick favorite button
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(FavoriteContract.MovieEntry.COLUMN_MOVIE_ID, movieModel.getID());
                    contentValues.put(FavoriteContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movieModel.getTitle());
                    contentValues.put(FavoriteContract.MovieEntry.COLUMN_POSTER_PATH, movieModel.getPosterPath());
                    contentValues.put(FavoriteContract.MovieEntry.COLUMN_OVERVIEW, movieModel.getOverview());
                    contentValues.put(FavoriteContract.MovieEntry.COLUMN_VOTE_AVERAGE, movieModel.getVoteAverage());
                    contentValues.put(FavoriteContract.MovieEntry.COLUMN_RELEASE_DATE, movieModel.getReleaseDate());
                    Uri uri = getContentResolver().insert(FavoriteContract.MovieEntry.CONTENT_URI, contentValues);
                    if (uri != null) {
                        Toast.makeText(getBaseContext(), movieModel.getTitle() + " added to favorites",
                                Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });

    }

    public class ReviewsTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            /* this line to clear the arrayList because it will duplicate the list of movies when we choose top_rated
            or popular movies if we do not clear it here  in onPreExecute after AsyncTask has finished
            */
            //   reviewsList.clear();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // 200 is mean everything is normal and HTTP Request is OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder reader = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {

                        reader.append(line);
                    }
                    // response is json result from API
                    parseResult(reader.toString());
                    result = 1; // this is mean fetching data is successful
                } else {
                    result = 0; // but here mean failed to fetch data
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {

            if (result == 1) {

                reviewAdapter = new ReviewAdapter(getApplicationContext(), reviewsList);
                ListViewReviews.setAdapter(reviewAdapter);

            } else {
                Toast.makeText(DetailsView.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // this method to parse data of json  to the arrayList and calling it in doInBackground method
    public void parseResult(String result) {
        try {
            // JSONObject r = new JSONObject(movieModel.getID().toString());
            JSONObject Root = new JSONObject(result);
            JSONArray Movies = Root.getJSONArray(Result);

            for (int i = 0; i < Movies.length(); i++) {
                JSONObject post = Movies.getJSONObject(i);
                ReviewModel reviewModel = new ReviewModel();
                reviewModel.setId(post.getString(IIID));
                reviewModel.setAuthor(post.getString(Author));
                reviewModel.setContent(post.getString(Content));


                reviewsList.add(reviewModel);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    AsynkTask for Trailers
     */
    public class TrailersTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            /* this line to clear the arrayList because it will duplicate the list of movies when we choose top_rated
            or popular movies if we do not clear it here  in onPreExecute after AsyncTask has finished
            */

        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // 200 is mean everything is normal and HTTP Request is OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder reader = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {

                        reader.append(line);
                    }
                    // response is json result from API
                    trailersResult(reader.toString());
                    result = 1; // this is mean fetching data is successful
                } else {
                    result = 0; // but here mean failed to fetch data
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {

            if (result == 1) {
                trailerAdapter = new TrailerAdapter(getApplicationContext(), trailersList);
                ListViewTrailers.setAdapter(trailerAdapter);


            } else {
                Toast.makeText(DetailsView.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // gridViewTrailers
    // this method to parse data of json  to the arrayList and calling it in doInBackground method
    public void trailersResult(String result) {
        try {
            JSONObject Root = new JSONObject(result);
            JSONArray Movies = Root.getJSONArray(Result);

            for (int i = 0; i < Movies.length(); i++) {
                JSONObject post = Movies.getJSONObject(i);
                TrailersModel trailersModel = new TrailersModel();
                trailersModel.setKey(post.getString(Key));
                trailersModel.setName(post.getString(Name));
                trailersList.add(trailersModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
