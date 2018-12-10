package com.jman.capstone_project;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jman.capstone_project.adapters.PlaceListAdapter;
import com.jman.capstone_project.database.entities.Place;
import com.jman.capstone_project.viewmodel.PlacesViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesFragment extends Fragment {

    private PlacesViewModel mPlacesViewModel;

    public PlacesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_places, container,
                false);

        // bind recyclerView with XML recyclerView declaration
        RecyclerView recyclerView = rootView.findViewById(R.id.places_recyclerview);

        // init & set adapter
        final PlaceListAdapter mAdapter = new PlaceListAdapter(getContext());
        recyclerView.setAdapter(mAdapter);

        // attach recyclerView and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mPlacesViewModel = ViewModelProviders.of(this).get(PlacesViewModel.class);

        mPlacesViewModel.getAllWords().observe(this, new Observer<List<Place>>() {
            @Override
            public void onChanged(@Nullable final List<Place> places) {
                // Update the cached copy of the places in the adapter.
                mAdapter.setPlaces(places);
            }
        });


        return rootView;
    }

}
