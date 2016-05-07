package com.movies.popular.jrvm.com.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    final String TITLE_EXTRA = "title_extra";
    final String POSTER_EXTRA = "poster_extra";
    final String SYNOPSIS_EXTRA = "synopsis_extra";
    final String RATING_EXTRA = "rating_extra";
    final String RELEASE_DATE_EXTRA = "release_date_extra";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ImageView posterDetail = (ImageView) findViewById(R.id.detail_image);
        TextView synopsisDetail = (TextView) findViewById(R.id.detail_synopsis);
        TextView ratingDetail = (TextView) findViewById(R.id.detail_rating);
        TextView releasedateDetail = (TextView) findViewById(R.id.detail_releasedate);

        Bundle extrasIntent = getIntent().getExtras();

        getSupportActionBar().setTitle(extrasIntent.getString(TITLE_EXTRA));
        Picasso.with(getApplication()).load(getIntent().getStringExtra(POSTER_EXTRA)).into(posterDetail);
        synopsisDetail.setText(extrasIntent.getString(SYNOPSIS_EXTRA));
        ratingDetail.setText("Users Ratings: " + extrasIntent.getString(RATING_EXTRA));
        releasedateDetail.setText("Release Date: " + extrasIntent.getString(RELEASE_DATE_EXTRA));

    }
}
