package com.example.android.popularmoviesapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.TrailersModel;

import java.util.ArrayList;

/**
 * Created by azza anter on 3/2/2018.
 */

public class TrailerAdapter extends ArrayAdapter<TrailersModel> {
    Context context;

    public TrailerAdapter(Context context, ArrayList<TrailersModel> trailers) {
        super(context, 0, trailers);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View LisItemView = convertView;
        if (LisItemView == null) {
            LisItemView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_item, parent, false);
        }
        final TrailersModel currentTrailers = getItem(position);
        TextView name = (TextView) LisItemView.findViewById(R.id.TV_title_trailer);
        assert currentTrailers != null;
        name.setText(currentTrailers.getName());
        ImageButton imageButton = (ImageButton) LisItemView.findViewById(R.id.playButton);
        context = LisItemView.getContext();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("https://www.youtube.com/watch?v=" + currentTrailers.getKey());
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                launchBrowser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(launchBrowser);
            }

        });
        return LisItemView;
    }


}

