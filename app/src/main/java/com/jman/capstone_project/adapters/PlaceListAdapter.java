package com.jman.capstone_project.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jman.capstone_project.R;
import com.jman.capstone_project.database.entities.Place;

import java.util.List;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.PlaceViewHolder> {

    class PlaceViewHolder extends RecyclerView.ViewHolder {
        private final TextView placeItemTextView;

        private PlaceViewHolder(View itemView) {
            super(itemView);
            placeItemTextView = itemView.findViewById(R.id.place_list_item_textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Place> mPlaces; // Cached copy of places


    public PlaceListAdapter(Context context) {

        // mInflater is initialised here to avoid declaring a new Context field
        // context is only really going to be using in onCreateViewHolder
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View placeItemView = mInflater.inflate(R.layout.placesrecyclerview_item, viewGroup, false);

        return new PlaceViewHolder(placeItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder placeViewHolder, int position) {
        if(mPlaces != null) {
            Place currentPlace = mPlaces.get(position);
            placeViewHolder.placeItemTextView.setText(currentPlace.getCityName());
        }
    }

    /*
     * Updates list of words from db
     *
     **/
//    public void setPlaces(List<Place> places){
//        mPlaces = places;
//        notifyDataSetChanged();
//    }

    public void setPlaces(List<Place> places){
        mPlaces = places;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mPlaces != null) {
            return mPlaces.size();
        } else {
            return 0;
        }
    }
}
