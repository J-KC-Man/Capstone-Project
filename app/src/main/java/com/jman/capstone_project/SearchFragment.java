package com.jman.capstone_project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.jman.capstone_project.viewmodel.PlacesViewModel;

import java.util.concurrent.TimeoutException;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

import static com.jman.capstone_project.global.Constants.BASE_URL_LOCATION_SEARCH;
import static com.jman.capstone_project.global.Constants.BASE_URL_LOCATION_SEARCH_LON;
import static com.jman.capstone_project.global.Constants.BASE_URL_TEXT_SEARCH;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class SearchFragment extends Fragment {

    /*
    * This interface is for accessing the isOnline method in MainActivity
    * */
    public interface CheckConnectivityCallback{
        boolean isOnline();
    }

    /*
    * callback object is used in search()
    * */
    private CheckConnectivityCallback callback;
    public void onAttach(Context context){
        callback = (CheckConnectivityCallback) context;
        super.onAttach(context);
    }

    private PlacesViewModel placesViewModel;

    @Nullable
    @BindView(R2.id.search_editText) EditText searchEditText;
    @Nullable
    @BindView(R2.id.search_button) Button searchButton;
    @Nullable
    @BindView(R2.id.error_message_textView) TextView resultMessageTextView;
    @Nullable
    @BindView(R2.id.use_location_textView) TextView searchByLocationTextView;

    private Unbinder unbinder;

    /* FusedLocationProvider api*/
    private FusedLocationProviderClient mFusedLocationClient;
    private double mLatitude = 0.0, mLongitude = 0.0;
    private int locationRequestCode = 1000;

    // lists for permissions
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000; // = 5 seconds


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        AdView mAdView = rootView.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL * 2); // 10 seconds
        locationRequest.setFastestInterval(FASTEST_INTERVAL); // 5 seconds

        // this is invoked when the location has been requested and recieved
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();
                    }
//                    if (mFusedLocationClient != null) {
//                        mFusedLocationClient.removeLocationUpdates(locationCallback); // put this in onPause
//                    }
                }
            }
        };

        searchByLocationTextView.setOnClickListener(v -> {
            getLocation();
            Log.d("SearchFragment.class", "Long and lat= " + mLongitude + ", " + mLatitude);

            // if long and lat have been updated. use coordinates to create url to pass into AsyncTask
            if(mLongitude != 0.0 && mLatitude != 0.0) {
                // create URL to pass into AsyncTask
                resultMessageTextView.setText(R.string.search_loading_text);
                try {
                    search(createUrlFromLocation(mLongitude, mLatitude));
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }

        });

        // restore result text if device was rotated after a search was done
        if (savedInstanceState != null) {
            String s = savedInstanceState.getString("result");
            resultMessageTextView.setText(s);
            Toast.makeText(getContext(), "Saved value: " + s, Toast.LENGTH_LONG).show();
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
   //     outState.putString("result",resultMessageTextView.getText().toString());
        super.onSaveInstanceState(outState);
    }



    private void getLocation() {
        //Now ask for runtime permission for above android 6 OS devices
        if (checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // api level 23
                // request for permission if not granted and get result on onRequestPermissionsResult overridden method
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        locationRequestCode);
            }


        }
        // permission already granted
        else {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: { // the 1000 request is defined by you, helps to id it
                // If request is cancelled, the result arrays are empty. ie, user doesn;t click anything but presses back button
                // the rest of the code is run if the user clicks yes or no:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission is granted

                    mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                        if (location != null) {
                            mLatitude = location.getLatitude();
                            mLongitude = location.getLongitude();

                        } else {
                            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                        }
                    });

                }
                // user chooses not to grant permissions
                else {
                    Toast.makeText(getContext(), R.string.grant_location_permission_denied, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
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
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), R.string.keyboard_already_hidden, Toast.LENGTH_SHORT);
                }

                String searchTerm = searchEditText.getText().toString();
                // pass edit text input to validate
                if (validate(searchTerm) == true) {
                    resultMessageTextView.setText(R.string.search_loading_text);
                    try {
                        search(createUrlFromText(searchTerm));
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                } else { // there was an issue with the place name
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
                ) {
            isValid = false;
        } else {
            isValid = true;
        }

        return isValid;
    }

    public String createUrlFromText(String queryParams) {

        String url = BASE_URL_TEXT_SEARCH + queryParams.replaceAll("\\s","");

        return url;
    }

    /*
     * called when location is used for search
     */
    public String createUrlFromLocation(Double longitude, Double latitude) {
        // might need to round doubles and covert to strings

        String url = BASE_URL_LOCATION_SEARCH + latitude.toString() + BASE_URL_LOCATION_SEARCH_LON + longitude.toString();

        return url;
    }

    public void search(String queryParams) throws TimeoutException {

        // check if device has network connection
        if(!callback.isOnline()) {
            Toast.makeText(
                    getContext(),
                    R.string.no_internet_connection,
                    Toast.LENGTH_LONG).show();
            throw new TimeoutException("Connect timeout: no network connection");
        }
        placesViewModel.makeApiCall(queryParams);
        placesViewModel.getCityId().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String cityId) {
                if (cityId == null) {
                    // set text of error message
                    resultMessageTextView.setText(R.string.search_invalid_text);
                    return;
                }
                resultMessageTextView.setText(R.string.search_success_text);
                resultMessageTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

            }
        });
    }

    /*
     * User returns to the fragment
     * */
    @Override
    public void onResume() {
        super.onResume();

        if (!checkPlayServices()) {
            resultMessageTextView.setText(R.string.missing_google_play_services_msg);
        }
        if (checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // api level 23
                // request for permission if not granted and get result on onRequestPermissionsResult overridden method
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        locationRequestCode);
            }

        } else { // permission already granted
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }

    }

    /*
    * Called when the user leaves the fragment
    * */
    @Override
    public void onPause() {
        super.onPause();

        // stop the location updates
        mFusedLocationClient.removeLocationUpdates(locationCallback);

    }

    /*
    * Call this method in onResume
    * Source: https://medium.com/@ssaurel/getting-gps-location-on-android-with-fused-location-provider-api-1001eb549089
    * with guidance from https://developers.google.com/android/guides/setup (Ensure Devices Have the Google Play services APK)
    * Author: Sylvain Saurel
    * */
    private boolean checkPlayServices() {
        //get a reference to the singleton object that provides isGooglePlayServicesAvailable() method
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getContext());

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                // provide error message in dialog and action for user to click on to update play services
                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                getActivity().finish();
            }
            // there is an issue
            return false;
        }
        // there is no issue
        return true;
    }


}
