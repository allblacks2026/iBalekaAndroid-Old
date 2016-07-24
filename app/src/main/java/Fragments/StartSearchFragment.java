package Fragments;


<<<<<<< HEAD
=======
import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
>>>>>>> 6563102e0688568dacf9c9cc64df6123baa27909
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import Listeners.MainActivityListener;
import allblacks.com.Activities.R;
=======
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.List;

import BackgroundTasks.SearchEventsBackgroundTask;
import Utilities.DeviceHardwareChecker;
import allblacks.com.iBaleka.R;
>>>>>>> 6563102e0688568dacf9c9cc64df6123baa27909

/**
 * A simple {@link Fragment} subclass.
 */
<<<<<<< HEAD
public class StartSearchFragment extends Fragment {

    private CheckBox sortResultsCheckBox;
    private EditText searchCriteriaCheckBox;
    private Button searchEventsButton;
    private MainActivityListener buttonListener;
=======
public class StartSearchFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private RecyclerView searchResultsRecyclerView;
    private Place selectedPlace;
    private String currentLocation;
    private GoogleApiClient googleApiClient;
    private boolean resolvingError = false;
    private static final int RESOLVE_CONNECTION_ERROR = 100;
    private static final String DIALOG_ERROR = "dialog_error";
    private static final String RESOLVING_ERROR = "resolving_error";
    private boolean permissionGranted = false;
    private static final int ACCESS_FINE_LOCATION_PERMISSION = 150;
    private List<Place> likelyPlaces = new ArrayList<>();
    private PlaceAutocompleteFragment autoCompleteFragment;

>>>>>>> 6563102e0688568dacf9c9cc64df6123baa27909
    public StartSearchFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View currentView =  inflater.inflate(R.layout.fragment_start_search, container, false);
<<<<<<< HEAD
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        searchCriteriaCheckBox = (EditText) currentView.findViewById(R.id.SearchCriteriaEditText);
        sortResultsCheckBox = (CheckBox) currentView.findViewById(R.id.SortByDateCheckBox);
        searchEventsButton = (Button) currentView.findViewById(R.id.SearchEventsButton);
        buttonListener = new MainActivityListener(getActivity());
        searchEventsButton.setOnClickListener(buttonListener);
    }

=======
        resolvingError = savedInstanceState != null && savedInstanceState.getBoolean(RESOLVING_ERROR, false);
        initializeComponents(currentView, savedInstanceState);
        buildGoogleApi();
        handlePermissions();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getDeviceSuggestedLocations();
        }
        return currentView;
    }

    private void initializeComponents(final View currentView, Bundle savedInstanceState) {
        searchResultsRecyclerView = (RecyclerView) currentView.findViewById(R.id.EventSearchResultsRecyclerView);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (savedInstanceState == null) {
            autoCompleteFragment = (PlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.GoogleSearchFragment);
            autoCompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    currentLocation = place.getName().toString();
                    selectedPlace = place;
                    processSearch();
                }

                @Override
                public void onError(Status status) {
                    displayMessage("Error Getting Location", status.getStatusMessage());
                }
            });
        }

    }

    private void processSearch() {
        DeviceHardwareChecker checker = new DeviceHardwareChecker(getActivity());
        checker.checkNetworkConnection();
        if (checker.isConnectedToInternet()) {

            clearRecyclerView();
            String placeName = selectedPlace.getName().toString();
            String telephoneNumber = selectedPlace.getPhoneNumber().toString();
            SearchEventsBackgroundTask backgroundTask = new SearchEventsBackgroundTask(getActivity());
            backgroundTask.execute(placeName, telephoneNumber);

        } else {
            displayMessage("Check Your Internet Connection", "You are not connected to the internet. Please check your internet connection");
        }
    }

    private void clearRecyclerView()
    {
        searchResultsRecyclerView.removeAllViews();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(RESOLVING_ERROR, resolvingError);
    }

    private void buildGoogleApi()
    {
        googleApiClient = new GoogleApiClient.Builder(getActivity(), this, this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(getActivity(), "Connected to Google", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if (resolvingError) {
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                resolvingError = true;
                connectionResult.startResolutionForResult(getActivity(), RESOLVE_CONNECTION_ERROR);
            } catch (Exception error) {
                displayMessage("Error Resolving Google Connection", error.getMessage());
                googleApiClient.connect();
            }
        } else {
            resolvingError = true;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESOLVE_CONNECTION_ERROR) {
            resolvingError = false;

            if (resultCode == getActivity().RESULT_OK) {
                if (!googleApiClient.isConnecting() && !googleApiClient.isConnected()) {
                    googleApiClient.connect();
                }
            }
        }
    }

    public void displayMessage(String title, String message) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
        messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        messageBox.show();
    }

    public void handlePermissions()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                getDeviceSuggestedLocations();
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                    displayMessage("Permission to use GPS", "The iBaleka app needs access to your GPS receiver");
                }
                requestPermissions(new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ACCESS_FINE_LOCATION_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                displayMessage("Search Not Available", "The Search feature will not be as effective as it should be due to permission being denied");
            }
        }
    }

    private void getDeviceSuggestedLocations()
    {
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(googleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(@NonNull PlaceLikelihoodBuffer placeLikelihoods) {
                for (PlaceLikelihood placeLikelihood : placeLikelihoods)
                {
                    likelyPlaces.add(placeLikelihood.getPlace());
                }
                placeLikelihoods.release();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        buildGoogleApi();
    }

    @Override
    public void onDestroy() {
        //Link: http://stackoverflow.com/questions/20919048/android-android-view-inflateexception-binary-xml-file-line-8-error-inflatin
        super.onDestroy();
        final FragmentManager manager = getFragmentManager();
        final Fragment searchFragment = manager.findFragmentById(R.id.GoogleSearchFragment);
        if (searchFragment != null) {
            manager.beginTransaction().remove(searchFragment).commit();
        }
    }
>>>>>>> 6563102e0688568dacf9c9cc64df6123baa27909
}
