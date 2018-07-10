package com.example.android.popularmoviesapp;

import android.app.Activity;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.popularmoviesapp.Adapter.MoviesAdapter;
import com.example.android.popularmoviesapp.data.FavoriteContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DiscoveryScreen extends AppCompatActivity implements MoviesAdapter.ListItemClickListener {
    private static final String TAG = "RecyclerViewForMovies";
   public String urlPopular = "https://api.themoviedb.org/3/movie/popular?api_key=d643f9d3c6ef8fc8c7aa952ba183c80a";
    public String urlTop = "https://api.themoviedb.org/3/movie/top_rated?api_key=d643f9d3c6ef8fc8c7aa952ba183c80a";

    public static List<MovieModel> MoviesList = new ArrayList<>();
    public static List<MovieModel> FavoritesList = new ArrayList<>();
    private MoviesAdapter favoAdapter;

    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    public final static String LIST_STATE_KEY = "recycler_list_state";
    Parcelable listState;

    private static final String[] MOVIE_COLUMNS = {


            FavoriteContract.MovieEntry.COLUMN_MOVIE_ID,
            FavoriteContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            FavoriteContract.MovieEntry.COLUMN_POSTER_PATH,
            FavoriteContract.MovieEntry.COLUMN_OVERVIEW,
            FavoriteContract.MovieEntry.COLUMN_RELEASE_DATE,
            FavoriteContract.MovieEntry.COLUMN_VOTE_AVERAGE};

    private RecyclerView mRecyclerView;
    private MoviesAdapter adapter;
    public static final String Result = "results";
    public static final String Movie_ID = "id";
    public static final String Title = "original_title";
    public static final String Poster = "poster_path";
    public static final String OverView = "overview";
    public static final String VoteAverage = "vote_average";
    public static final String ReleaseDate = "release_date";
    private ProgressBar progressBar;
    GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_screen);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_Movies);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        progressBar = (ProgressBar) findViewById(R.id.pb_movies);
        new MoviesTask().execute(urlPopular);

    }

    public List<MovieModel> bn() {

        Cursor cursor = getBaseContext().getContentResolver().query(FavoriteContract.MovieEntry.CONTENT_URI, MOVIE_COLUMNS, null, null
                , null);
        if (cursor != null & cursor.moveToFirst()) {
            FavoritesList.clear();
            while (cursor.moveToNext()) {
                Integer movieID = cursor.getInt(0);
                String movieTitle = cursor.getString(1);
                String moviePosterPath = cursor.getString(2);
                String movieOverview = cursor.getString(3);
                String movieDate = cursor.getString(4);
                String movieRate = cursor.getString(5);
                MovieModel md = new MovieModel(movieID, movieTitle, moviePosterPath, movieOverview, movieDate, movieRate);
                FavoritesList.add(md);

            }
            cursor.close();

        }
        return FavoritesList;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        //Toast.makeText(this,"clicked",Toast.LENGTH_SHORT).show();
    }

    public class MoviesTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {

            progressBar.setVisibility(View.VISIBLE);
            /* this line to clear the arrayList because it will duplicate the list of movies when we choose top_rated
            or popular movies if we do not clear it here  in onPreExecute after AsyncTask has finished
            */
            MoviesList.clear();
            FavoritesList.clear();
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
            progressBar.setVisibility(View.GONE);

            if (result == 1) {
                adapter = new MoviesAdapter(MoviesList, getApplicationContext(), DiscoveryScreen.this);
                mRecyclerView.setAdapter(adapter);


            } else {
                Toast.makeText(DiscoveryScreen.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // this method to parse data of json  to the arrayList and calling it in doInBackground method
    public void parseResult(String result) {
        try {
            JSONObject Root = new JSONObject(result);
            JSONArray Movies = Root.getJSONArray(Result);

            for (int i = 0; i < Movies.length(); i++) {
                JSONObject post = Movies.getJSONObject(i);
                MovieModel movieItem = new MovieModel();
                movieItem.setID(post.getInt(Movie_ID));
                movieItem.setTitle(post.getString(Title));
                movieItem.setPosterPath(post.getString(Poster));
                movieItem.setOverview(post.getString(OverView));
                movieItem.setVoteAverage(post.getString(VoteAverage));
                movieItem.setReleaseDate(post.getString(ReleaseDate));
                MoviesList.add(movieItem);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        FavoritesList.clear();
        bn();
    }
    @Override
    protected void onPause()
    {
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.top_rated) {
          new MoviesTask().execute(urlTop);

        } else if (itemThatWasClickedId == R.id.most_popular) {
           // url = "https://api.themoviedb.org/3/movie/popular?api_key=d643f9d3c6ef8fc8c7aa952ba183c80a";
            new MoviesTask().execute(urlPopular);
        } else if (itemThatWasClickedId == R.id.favorite_popular) {
            favoAdapter = new MoviesAdapter(bn(), getApplicationContext(), DiscoveryScreen.this);
            mRecyclerView.setAdapter(favoAdapter);

        }
        return super.onOptionsItemSelected(item);
    }


}
