package com.jman.capstone_project;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jman.capstone_project.adapters.PlaceListAdapter;
import com.jman.capstone_project.database.entities.Place;
import com.jman.capstone_project.viewmodel.IViewModelCallback;
import com.jman.capstone_project.viewmodel.PlacesViewModel;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesFragment extends Fragment implements IViewModelCallback {

    private PlacesViewModel mPlacesViewModel;

    public PlacesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
        final PlaceListAdapter mAdapter = new PlaceListAdapter(getContext(), this);
        recyclerView.setAdapter(mAdapter);

        // attach recyclerView and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Add the functionality to swipe items in the
        // recycler view to delete that item
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Place place= mAdapter.getPlaceAtPosition(position);
                        Toast.makeText(getContext(), "Deleting " +
                                place.getCityName(), Toast.LENGTH_LONG).show();

                        // Delete the word
                        mPlacesViewModel.deletePlace(place);
                    }
                });

        helper.attachToRecyclerView(recyclerView);

        mPlacesViewModel = ViewModelProviders.of(this).get(PlacesViewModel.class);

        mPlacesViewModel.getAllPlaces().observe(this, new Observer<List<Place>>() {
            @Override
            public void onChanged(@Nullable final List<Place> places) {
                // Update the cached copy of the places in the adapter.
                mAdapter.setPlaces(places);
            }
        });


        return rootView;
    }

    @Override
    public void passToViewModel(int position) {
        mPlacesViewModel.setPosition(position);
        Place place = mPlacesViewModel.getAllPlaces().getValue().get(position);
        // load fragment from MainActivity and pass in Place
        // This tightly couples the fragment to the underlying activity
        // an improvement would be to use another interface callback
        // or could call viewmodel and set place to livedata
        // in WeatherFrag I can retrieve the place and observe it
        ((MainActivity) getActivity()).loadWeatherFragment(place);

    }
}
