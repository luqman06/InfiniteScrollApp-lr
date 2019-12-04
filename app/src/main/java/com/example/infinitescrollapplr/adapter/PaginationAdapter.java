package com.example.infinitescrollapplr.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.infinitescrollapplr.R;
import com.example.infinitescrollapplr.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w500";

    private List<com.example.infinitescrollapplr.model.Movie> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapter (Context context){
        this.context = context;
        movieResults = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;

            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }


        return viewHolder;

    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater){
        View v1 = inflater.inflate(R.layout.activity_list, parent, false);

        return new MovieVH(v1);
    }
    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position){

        com.example.infinitescrollapplr.model.Movie result = movieResults.get(position); //movie

        switch (getItemViewType(position)){
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;
                movieVH.mMovieTitle.setText(result.getTitle());

                movieVH.mYear.setText(
                        result.getReleaseDate().substring(0, 4)//year only
                        + "|"
                        + result.getOriginalLanguage().toUpperCase()
                );
                movieVH.mMovieDesc.setText(result.getOverview());

                Glide.with(context)
                        .load(BASE_URL_IMG+result.getPosterPath())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(movieVH.mPosterImg);
                break;
            case LOADING:

                break;


        }
    }
    @Override
    public int getItemCount() {
        return movieResults == null ? 0 : movieResults.size();
    }

    @Override
    public int getItemViewType(int position){
        return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    //helper

    public void add(com.example.infinitescrollapplr.model.Movie r){
        movieResults.add(r);
        notifyItemInserted(movieResults.size()-1);
    }

    public void addAll(List<com.example.infinitescrollapplr.model.Movie> movieResults){
        for (com.example.infinitescrollapplr.model.Movie result : movieResults){
            add(result);
        }
    }

    public void remove(Movie r){
        int position = movieResults.indexOf(r);
        if (position > -1){
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }


    public void clear(){
        isLoadingAdded = false;
        while (getItemCount() > 0){
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter(){
        isLoadingAdded = true;
    }

    public void removeLoadingFooter(){
        isLoadingAdded = false;

        int position = movieResults.size() -1;
        com.example.infinitescrollapplr.model.Movie result = getItem(position);

        if (result !=null){
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public com.example.infinitescrollapplr.model.Movie getItem(int position){
        return movieResults.get(position);

    }


    //view result
    //main list center view holder

    protected class MovieVH extends RecyclerView.ViewHolder{
        private TextView mMovieTitle;
        private TextView mMovieDesc;
        private TextView mYear;
        private ImageView mPosterImg;
        private ProgressBar mProgress;
        public MovieVH(View itemView){
            super(itemView);

            mMovieTitle = itemView.findViewById(R.id.movie_title);
            mMovieDesc = itemView.findViewById(R.id.movie_desc);
            mYear = itemView.findViewById(R.id.movie_year);
            mPosterImg = itemView.findViewById(R.id.movie_poster);
            mProgress = itemView.findViewById(R.id.movie_progress);
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder{
        public LoadingVH(View itemView){
            super(itemView);
        }
    }
}
