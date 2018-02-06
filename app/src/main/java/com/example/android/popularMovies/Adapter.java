package com.example.android.popularMovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularMovies.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.GridViewHolder> {


    private int mNumberItems;
    private List<ArrayList> popularMovieData;
    private Context context;
    private final MovieAdapterOnClickHandler clickHandler;

    public Adapter(MovieAdapterOnClickHandler cHandler)
    {

        clickHandler = cHandler;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(List movieDetails);
    }
    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_grid_view, viewGroup, false);
        return  new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position){

        holder.bind(position);
    }

    @Override
    public int getItemCount(){
        return mNumberItems;
    }

    class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final ImageView imageView;
         Uri link ;

        public GridViewHolder(View itemView){

            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.movieImage);
            itemView.setOnClickListener(this);
            }

        void bind (int listPosition)
        {
//fetching posterPath from list
            if(!popularMovieData.isEmpty()) {

                String ll = popularMovieData.get(listPosition).get(3).toString();
                link = NetworkUtils.buildUri(ll);
                Picasso.with(context).load(link).into(imageView);
            }
        }

        @Override
        public void onClick(View view){
            int adapterPosition = getAdapterPosition();
            if(!popularMovieData.isEmpty()) {
                List selectedMovieDetails = popularMovieData.get(adapterPosition);
                clickHandler.onClick(selectedMovieDetails);
            }
        }
    }

    public void setPMData(List<ArrayList> pmData) {
        if(!pmData.isEmpty()) {
            popularMovieData = pmData;
            mNumberItems = pmData.size();
        }else{
            popularMovieData = null;
            mNumberItems = 0;
        }
        notifyDataSetChanged();
    }


}