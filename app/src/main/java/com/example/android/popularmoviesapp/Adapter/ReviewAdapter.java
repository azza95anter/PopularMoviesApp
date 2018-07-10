package com.example.android.popularmoviesapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.ReviewModel;

import java.util.ArrayList;

/**
 * Created by azza anter on 3/2/2018.
 */

public class ReviewAdapter extends ArrayAdapter<ReviewModel> {
    public ReviewAdapter(Context context, ArrayList<ReviewModel> reviews) {
        super(context, 0, reviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View LisItemView = convertView;
        if (LisItemView == null) {
            LisItemView = LayoutInflater.from(getContext()).inflate(R.layout.reviews_item, parent, false);
        }
        ReviewModel currentReview = getItem(position);
        TextView Author = (TextView) LisItemView.findViewById(R.id.tv_Review_Author);
        Author.setText(currentReview.getAuthor());

        TextView content = (TextView) LisItemView.findViewById(R.id.tv_Content);
        content.setText(currentReview.getContent());
        return LisItemView;
    }
}
