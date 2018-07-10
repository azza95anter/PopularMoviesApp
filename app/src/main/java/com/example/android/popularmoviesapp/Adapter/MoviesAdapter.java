package com.example.android.popularmoviesapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.DetailsView;
import com.example.android.popularmoviesapp.MovieModel;
import com.example.android.popularmoviesapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by azza anter on 2/1/2018.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private List<MovieModel> objectMovieModels;
    private Context context;

    final String Base_URL = "https://image.tmdb.org/t/p/w185";
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    // constructor
    public MoviesAdapter(List<MovieModel> objectMovieModels, Context context, ListItemClickListener listener) {
        this.objectMovieModels = objectMovieModels;
        this.context = context;
        mOnClickListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, null);
        MovieViewHolder viewHolder = new MovieViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MovieModel movieModel = objectMovieModels.get(position);
        Picasso.with(context).load(Base_URL + movieModel.getPosterPath()).into(holder.imageView);
        holder.title.setText(movieModel.getTitle());
    }

    @Override
    public int getItemCount() {
        return objectMovieModels.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public TextView title;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageID);
            title = (TextView) itemView.findViewById(R.id.TV_title);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
            MovieModel movieModel = objectMovieModels.get(clickedPosition);
            Intent intent = new Intent(v.getContext(), DetailsView.class);
            intent.putExtra("MovieModel", movieModel);
            v.getContext().startActivity(intent);


        }

    }
}
