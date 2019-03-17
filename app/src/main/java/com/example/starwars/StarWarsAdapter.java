package com.example.starwars;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.starwars.utils.StarWarsUtils;

import java.util.ArrayList;
import java.lang.String;

public class StarWarsAdapter extends RecyclerView.Adapter<StarWarsAdapter.StarWarsItemViewHolder>{
    private ArrayList<StarWarsUtils.starwarsItem> mStarwarsItems;
    private OnStarWarsItemClickListener mStarWarsItemClickListener;

    public interface OnStarWarsItemClickListener {
        void onStarWarsItemClick(StarWarsUtils.starwarsItem starwarsItem);
    }

    public StarWarsAdapter(OnStarWarsItemClickListener clickListener){
        mStarWarsItemClickListener = clickListener;
    }

    public void updateStarWarsItems(ArrayList<StarWarsUtils.starwarsItem> starwarsItems){
        mStarwarsItems = starwarsItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        if (mStarwarsItems != null){
            return mStarwarsItems.size();
        } else {
            return 0;
        }
    }


    @Override
    public StarWarsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.forecast_list_item, parent, false);

        return new StarWarsItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StarWarsItemViewHolder starWarsItemViewHolder, int i) {
        starWarsItemViewHolder.bind(mStarwarsItems.get(i));
    }

    class StarWarsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mStarWarsNameTV;


        public StarWarsItemViewHolder(View itemView){
            super(itemView);

            mStarWarsNameTV = (TextView)itemView.findViewById(R.id.tv_forecast_temp_description);
            itemView.setOnClickListener(this);
        }

        public void bind(StarWarsUtils.starwarsItem starwarsItem){

            String detailString = starwarsItem.name;
            mStarWarsNameTV.setText(detailString);
        }

        @Override
        public void onClick(View v){
            StarWarsUtils.starwarsItem starwarsItem = mStarwarsItems.get(getAdapterPosition());
            mStarWarsItemClickListener.onStarWarsItemClick(starwarsItem);
        }
    }
}
