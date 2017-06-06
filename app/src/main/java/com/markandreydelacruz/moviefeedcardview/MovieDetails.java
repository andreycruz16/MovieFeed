package com.markandreydelacruz.moviefeedcardview;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.markandreydelacruz.moviefeedcardview.Models.MovieModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetails extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviedetails);
        // Showing and Enabling clicks on the Home/Up button
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setTitle("Details");

        final ProgressBar progressBarDetailsMovie = (ProgressBar) findViewById(R.id.progressBarDetailsMovie);
        ImageView imageViewDetailsMovie = (ImageView) findViewById(R.id.imageViewDetailsMovie);
        RatingBar ratingBarDetailsRating = (RatingBar) findViewById(R.id.ratingBarDetailsRating);
        TextView textViewDetailsTitle = (TextView) findViewById(R.id.textViewDetailsTitle);
        TextView textViewDetailsTagline = (TextView) findViewById(R.id.textViewDetailsTagline);
        TextView textViewDetailsYear = (TextView) findViewById(R.id.textViewDetailsYear);
        TextView textViewDetailsDuration = (TextView) findViewById(R.id.textViewDetailsDuration);
        TextView textViewDetailsCast = (TextView) findViewById(R.id.textViewDetailsCast);
        TextView textViewDetailsStory = (TextView) findViewById(R.id.textViewDetailsStory);

        Bundle bundle = getIntent().getExtras();
        String json = bundle.getString("movieDetails"); // getting the model from MainActivity send via extras
        MovieModel movieModel = new Gson().fromJson(json, MovieModel.class);


        ImageLoader.getInstance().displayImage(movieModel.getImage(), imageViewDetailsMovie, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBarDetailsMovie.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBarDetailsMovie.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBarDetailsMovie.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressBarDetailsMovie.setVisibility(View.GONE);
            }
        });
        ratingBarDetailsRating.setRating((float) (movieModel.getRating() / 2));
        textViewDetailsTitle.setText(movieModel.getMovie());
        textViewDetailsTagline.setText(movieModel.getTagline());
        textViewDetailsYear.setText(String.valueOf(movieModel.getYear()));
        textViewDetailsDuration.setText(movieModel.getDuration());
        StringBuilder stringBuilder = new StringBuilder();
        for(MovieModel.CastBean cast : movieModel.getCast()){
            stringBuilder.append(cast.getName()).append(", ");
        }
        textViewDetailsCast.setText(String.format("Cast:%s", stringBuilder));
        textViewDetailsStory.setText(movieModel.getStory());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
