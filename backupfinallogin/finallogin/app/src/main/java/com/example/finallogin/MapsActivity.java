package com.example.finallogin;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;
    private static final String FINE_LOCATION= ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION=Manifest.permission.ACCESS_COARSE_LOCATION;
    boolean mLocationPermissionGranted=false;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1234;
    private Location mLastKnownLocation;
    private static final String TAG = "MapsActivity";
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private Context mContext;
    private TextView textView;
    private Button getPlace;
    private Button getTouristPlaces;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLocationPermission();
        getDeviceLocation();


//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        // Initialize the SDK
        Places.initialize(getApplicationContext(), "AIzaSyAXsq2GClhrh8yxDqVBiDubnBkUDwsNRPM");

        // Create a new Places client instance
        final PlacesClient placesClient = Places.createClient(this);
        textView=(TextView) findViewById(R.id.textView);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        //autocompleteFragment.setCountry("IN");
       // autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());


                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG);
                FetchPlaceRequest request = FetchPlaceRequest.newInstance(place.getId(), placeFields);

                placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                    Place place1 = response.getPlace();
                    LatLng latLng1= place1.getLatLng();
                    mLastKnownLocation.setLatitude(latLng1.latitude);
                    mLastKnownLocation.setLongitude(latLng1.longitude);
                    Toast.makeText(MapsActivity.this,"Location set successfully",Toast.LENGTH_LONG).show();
//                    textView.append("Place found: " + place1.getName());
//                    textView.append("Address: "+place1.getAddress());



                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        // Handle the error.
                    }
                });

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });




        mContext=getApplicationContext();
        getPlace=(Button) findViewById(R.id.getRestaurants);
        getPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick called");
                Object dataTransfer[] = new Object[1];
                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(MapsActivity.this);


                String restaurant = "restaurant";
                String url = getUrl(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), restaurant);
                dataTransfer[0] = url;
                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby Restaurants", Toast.LENGTH_SHORT).show();
            }
        });

        getTouristPlaces=(Button) findViewById(R.id.getTouristPlaces);
        getTouristPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick called");
                Object dataTransfer1[] = new Object[1];
                GetNearbyPlacesData getNearbyPlacesData1 = new GetNearbyPlacesData(MapsActivity.this);


                String tourist_attraction = "tourist_attraction";
                String url = getUrl(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), tourist_attraction);
                dataTransfer1[0] = url;
                getNearbyPlacesData1.execute(dataTransfer1);
                Toast.makeText(MapsActivity.this, "Showing Nearby Tourist attractions", Toast.LENGTH_SHORT).show();

            }
        });

        ListView listView=(ListView) findViewById(R.id.restaurants);
        listView.setVisibility(View.INVISIBLE);


    }


    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+3000);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyAXsq2GClhrh8yxDqVBiDubnBkUDwsNRPM");

        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

//    private void init()
//    {
//        Log.d(TAG,"init: initialising");
//        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if(actionId== EditorInfo.IME_ACTION_SEARCH
//                        || actionId==EditorInfo.IME_ACTION_DONE
//                        || event.getAction()==event.ACTION_DOWN
//                        || event.getAction()==event.KEYCODE_ENTER
//                         )
//                {
//                    InputMethodManager inputMethodManager = (InputMethodManager)
//                            mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    // Hide the soft keyboard
//                    inputMethodManager.hideSoftInputFromWindow(mSearchText.getWindowToken(),0);
//
//                    geoLocate();
//                }
//                return false;
//            }
//        });
//    }
//    private void geoLocate()
//    {
//        Log.d(TAG,"geoLocate: geolocating");
//        String searchString= autocompleteFragment.getText().toString();
//        Geocoder geocoder= new Geocoder(MapsActivity.this);
//        List<Address> list=new ArrayList<>();
//      try {
//          list=geocoder.getFromLocationName(searchString,1);
//      }catch (IOException e)
//      {
//          Log.e(TAG,"geoLocate: IOException "+e.getMessage());
//      }
//      if(list.size()>0)
//      {
//          Address address=list.get(0);
//          Log.d(TAG,"geoLocate: found a location "+address.toString());
//          Toast.makeText(this, "Address found", Toast.LENGTH_SHORT).show();
//
//          moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
//      }
//    }




    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermissionCalled");

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    Log.d(TAG,"Permission granted");
                }
            }
        }
        // updateLocationUI();
    }
//    private void updateLocationUI() {
//        if (mMap == null) {
//            return;
//        }
//        try {
//            if (mLocationPermissionGranted) {
//                mMap.setMyLocationEnabled(true);
//                mMap.getUiSettings().setMyLocationButtonEnabled(true);
//            } else {
//                mMap.setMyLocationEnabled(false);
//                mMap.getUiSettings().setMyLocationButtonEnabled(false);
//                mLastKnownLocation = null;
//                getLocationPermission();
//            }
//        } catch (SecurityException e)  {
//            Log.e("Exception: %s", e.getMessage());
//        }
//    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            Log.d(TAG,"Fetched location successfully");

                            //moveCamera(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()),DEFAULT_ZOOM,"My location");
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
//                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
//    private void moveCamera(LatLng latlng, float zoom, String title)
//    {
//        Log.d(TAG,"moveCamera: moving the camera to lat: "+latlng.latitude+", lng: "+latlng.longitude);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));
//
//        if(!title.equals("My location"))
//        {
//            MarkerOptions options=new MarkerOptions().position(latlng).title(title);
//            mMap.addMarker(options);
//        }
//    }
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
//
//        getLocationPermission();
//        updateLocationUI();
//
//        // Get the current location of the device and set the position of the map.
//        getDeviceLocation();
////        init();
//    }

}
