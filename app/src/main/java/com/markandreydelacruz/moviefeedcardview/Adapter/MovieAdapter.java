package com.markandreydelacruz.moviefeedcardview.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.markandreydelacruz.moviefeedcardview.Models.MovieModel;
import com.markandreydelacruz.moviefeedcardview.MovieDetails;
import com.markandreydelacruz.moviefeedcardview.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by mark on 6/1/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<MovieModel> movieModelList;
    private Context context;

    public MovieAdapter(List<MovieModel> movieModelList, Context context) {
        this.movieModelList = movieModelList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movies, parent, false); //false parameter is optional
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MovieModel movieModel = movieModelList.get(position);

        ImageLoader.getInstance().displayImage(movieModel.getImage(), holder.imageViewMoviePoster, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.progressBarMoviePoster.setVisibility(View.VISIBLE);
                holder.imageViewMoviePoster.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.progressBarMoviePoster.setVisibility(View.GONE);
                holder.imageViewMoviePoster.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.progressBarMoviePoster.setVisibility(View.GONE);
                holder.imageViewMoviePoster.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                holder.progressBarMoviePoster.setVisibility(View.GONE);
                holder.imageViewMoviePoster.setVisibility(View.INVISIBLE);
            }
        });

        holder.textViewTitle.setText(movieModel.getMovie());
        holder.textViewYear.setText(String.valueOf(movieModel.getYear()));
        holder.textViewTagline.setText(movieModel.getTagline());

        StringBuilder stringBuilder = new StringBuilder();
        for(MovieModel.CastBean castBean : movieModelList.get(position).getCast()){
            stringBuilder.append(castBean.getName()).append(", ");
        }
        holder.textViewCast.setText("Cast: " + stringBuilder);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(view.getContext(), "You clicked " + movieModel.getMovie(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(view.getContext(), MovieDetails.class);
                intent.putExtra("movieDetails" , new Gson().toJson(movieModel)); // converting model json into string type and sending it via intent
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewYear;
        TextView textViewTagline;
        TextView textViewCast;
        ProgressBar progressBarMoviePoster;
        ImageView imageViewMoviePoster;
        LinearLayout linearLayout;

        ViewHolder(View itemView) {
            super(itemView);

            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewYear= (TextView) itemView.findViewById(R.id.textViewYear);
            textViewTagline = (TextView) itemView.findViewById(R.id.textViewTagline);
            textViewCast = (TextView) itemView.findViewById(R.id.textViewCast);
            progressBarMoviePoster = (ProgressBar) itemView.findViewById(R.id.progressBarMoviePoster);
            imageViewMoviePoster = (ImageView) itemView.findViewById(R.id.imageViewMoviePoster);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);

        }
    }

}
