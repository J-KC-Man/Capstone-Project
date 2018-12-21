package com.jman.capstone_project;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jman.capstone_project.viewmodel.PlacesViewModel;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static com.jman.capstone_project.global.Constants.API_KEY;
import static com.jman.capstone_project.global.Constants.BASE_URL_LOCATION_SEARCH;
import static com.jman.capstone_project.global.Constants.BASE_URL_TEXT_SEARCH;


public class SearchFragment extends Fragment {

    private PlacesViewModel placesViewModel;
     LiveData<String> cityId;

    EditText searchEditText;
    Button searchButton;
    TextView resultMessageTextView;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        searchEditText = rootView.findViewById(R.id.search_editText);
        searchButton = rootView.findViewById(R.id.search_button);
        resultMessageTextView = rootView.findViewById(R.id.error_message_textView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Use ViewModelProviders to associate the ViewModel with the UI controller
        // this is the fragment, it serves as a view controller
        // When the activity is destroyed, for example through a configuration change, the ViewModel persists.
        // When the activity is re-created, the ViewModelProviders return the existing ViewModel.
        placesViewModel = ViewModelProviders.of(getActivity()).get(PlacesViewModel.class);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            try {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                Toast.makeText(getActivity(),"Keyboard already hidden", Toast.LENGTH_SHORT);
            }

            String searchTerm = searchEditText.getText().toString();
                // pass edit text input to validate
           if(validate(searchTerm) == true) {
               search(searchTerm);
           }
            else{
               resultMessageTextView.setText(R.string.search_validation_error);
               resultMessageTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorRed));
           }



            }
        });
    }

    public boolean validate(String s) {
        boolean isValid;

        if (s.equals("")
                || !s.matches("([A-Z][a-z]*,\\s*[A-Z]{2})")
     //           || !s.matches("([^\\s]+)")
                ){
            isValid = false;
        } else {
            isValid = true;
        }

        return isValid;
    }

    public void createUrlFromText(String queryParams) {
        URL url;
        try {
            url = new URL(BASE_URL_TEXT_SEARCH + URLEncoder.encode(queryParams, "UTF-8")
                    + "&APPID=" + API_KEY);

            // make api call with this URL
           // search(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /*
     * called when location is used for search
     */
    public void createUrlFromLocation(String queryParams) {
        URL url;
        try {
            url = new URL(BASE_URL_LOCATION_SEARCH + URLEncoder.encode(queryParams, "UTF-8")
                    + "&APPID=" + API_KEY);

            // make api call with this URL
           // search(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void search(String queryParams) {


        placesViewModel.makeApiCall(queryParams);
        placesViewModel.getCityId().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String cityId) {
                if(cityId == null) {
                    // set text of error message
                    resultMessageTextView.setText("not a valid place");
                    return;
                }
                resultMessageTextView.setText("Press Home to see weather");
                resultMessageTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

            }
        });
    }


}
