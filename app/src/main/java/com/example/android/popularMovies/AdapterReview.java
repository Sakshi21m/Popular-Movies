package com.example.android.popularMovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AdapterReview extends RecyclerView.Adapter<AdapterReview.LinearLayoutViewHolder>{


private int mNumberItems;
private List<ArrayList> reviewData;
private Context context;

public AdapterReview(Context context1)
{
    context = context1;

}
    @Override
    public LinearLayoutViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_linear_layout, viewGroup, false);
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

class LinearLayoutViewHolder extends RecyclerView.ViewHolder{

    final TextView textView1;
    final TextView textView2;


    public LinearLayoutViewHolder(View itemView){

        super(itemView);

        textView1 = (TextView) itemView.findViewById(R.id.Author);
        textView2 = (TextView) itemView.findViewById(R.id.Content);
    }

    void bind (int listPosition)
    {
        if(!reviewData.isEmpty()) {

            String author = reviewData.get(listPosition).get(1).toString();
            String content = reviewData.get(listPosition).get(2).toString();
            author = author+":";
                textView1.setText(author);
                textView2.setText(content);
        }
    }


}

    public void setPMData(List<ArrayList> pmData) {
        if(!pmData.isEmpty()) {
            reviewData = pmData;
            mNumberItems = pmData.size();
        }else{
            reviewData = null;
            mNumberItems = 0;
        }
        notifyDataSetChanged();
    }



}