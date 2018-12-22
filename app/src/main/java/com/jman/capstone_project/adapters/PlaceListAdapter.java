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
import com.jman.capstone_project.viewmodel.IViewModelCallback;

import java.util.List;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.PlaceViewHolder> {

    private final LayoutInflater mInflater;
    private List<Place> mPlaces; // Cached copy of places

    IViewModelCallback iViewModelCallback;


    public PlaceListAdapter(Context context, IViewModelCallback iViewModelCallback) {
        this.iViewModelCallback = iViewModelCallback;
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
            placeViewHolder.placeItemTextView.setText(
                    currentPlace.getCityName() + ", " + currentPlace.getCountry());
        }
    }

    /*
     * Updates list of words from db
     *
     **/
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

    class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView placeItemTextView;

        private PlaceViewHolder(View itemView) {
            super(itemView);
            placeItemTextView = itemView.findViewById(R.id.place_list_item_textView);

            itemView.setOnClickListener(PlaceViewHolder.this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it

                // pass to viewmodel using interface callback
                // viewmodel sets position in live data
                // Weather frag uses position to get the right record in mAllPlaces
                iViewModelCallback.passToViewModel(position);
            }
        }
    } // end of viewholder class
} // end of adapter class
