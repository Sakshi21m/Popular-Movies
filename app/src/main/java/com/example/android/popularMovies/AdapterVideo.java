package com.example.android.popularMovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AdapterVideo extends RecyclerView.Adapter<AdapterVideo.LinearLayoutViewHolder>{


private int mNumberItems;
private List<ArrayList> videoData;
private final MovieAdapterVideoOnClickHandler clickHandler;
    
public AdapterVideo(MovieAdapterVideoOnClickHandler cHandler)
        {
            clickHandler = cHandler;
        }

public interface MovieAdapterVideoOnClickHandler {
    void onClick(List movieDetails);
}
@Override
public LinearLayoutViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

     Context context;

    context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.video_movie_trailor, viewGroup, false);
        return  new LinearLayoutViewHolder(view);
        }

@Override
public void onBindViewHolder(LinearLayoutViewHolder holder, int position) {
        holder.bind(position);

        }

@Override
public int getItemCount(){
        return mNumberItems;
        }

class LinearLayoutViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    final TextView videoName;

    public LinearLayoutViewHolder(View itemView){

        super(itemView);

        videoName = (TextView)itemView.findViewById(R.id.videoName);

        itemView.setOnClickListener(this);
    }

    void bind (int listPosition)
    {
//fetching posterPath from list
        if(!videoData.isEmpty()) {

            String videoText = videoData.get(listPosition).get(4).toString();
            videoName.setText(videoText);
        }
    }


    @Override
    public void onClick(View view){
        int AdapterVideoPosition = getAdapterPosition();
        if(!videoData.isEmpty()) {
            List selectedMovieDetails = videoData.get(AdapterVideoPosition);
              clickHandler.onClick(selectedMovieDetails);
        }
    }
}

    public void setPMData(List<ArrayList> pmData) {
        if(!pmData.isEmpty()) {
            videoData = pmData;
            mNumberItems = pmData.size();
        }else{
            videoData = null;
            mNumberItems = 0;
        }
        notifyDataSetChanged();
    }



}