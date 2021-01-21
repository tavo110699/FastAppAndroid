package lunainc.com.mx.fastdelivery.UI.Activity.Client.Directions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;

import com.google.android.libraries.places.api.model.Place;

import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import lunainc.com.mx.fastdelivery.R;import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.com.mx.fastdelivery.Utils.Constants;

public class GetCurrentLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener {


    private static final int AUTOCOMPLETE_REQUEST_CODE = 22;
    private static final String TAG = GetCurrentLocationActivity.class.getSimpleName();


    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Location mLastKnownLocation;
    private LocationCallback locationCallback;

    private Geocoder geocoder;
    private List<Address> addresses;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.direction)
    TextView direction;

    @BindView(R.id.btn_find)
    Button btnFind;

    @BindView(R.id.ripple_bg)
    RippleBackground rippleBg;

    private View mapView;

    private final float DEFAULT_ZOOM = 18;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_current_location);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(GetCurrentLocationActivity.this);
        Places.initialize(GetCurrentLocationActivity.this, getString(R.string.MAP_VIEW_BUNDLE_KEY));






        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng currentMarkerLocation = mMap.getCameraPosition().target;
                rippleBg.startRippleAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rippleBg.stopRippleAnimation();

                        try {
                            addresses = geocoder.getFromLocation(currentMarkerLocation.latitude, currentMarkerLocation.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            setAddressInView(addresses.get(0).getAddressLine(0));


                            String directionComplete = addresses.get(0).getAddressLine(0);
                            String street = "";

                            if (addresses.get(0).getThoroughfare() != null){
                                street = addresses.get(0).getThoroughfare();
                            }


                            String city = "";
                            if (addresses.get(0).getLocality() != null) {
                                city = addresses.get(0).getLocality();
                            }

                            String colony = "";
                            if (addresses.get(0).getSubLocality() != null) {
                                colony = addresses.get(0).getSubLocality();
                            }
                            String state = "";
                            if (addresses.get(0).getAdminArea() != null) {
                                state = addresses.get(0).getAdminArea();
                            }

                            String country = "";
                            if (addresses.get(0).getCountryName() != null) {
                                country = addresses.get(0).getCountryName();
                            }

                            String number = "";
                            if (addresses.get(0).getFeatureName() != null) {
                                number = addresses.get(0).getFeatureName();
                            }

                            String postalCode = "";
                            if (addresses.get(0).getPostalCode() != null) {
                                postalCode = addresses.get(0).getPostalCode();
                            }




                            Intent intent = new Intent(GetCurrentLocationActivity.this, CreateDirectionActivity.class);
                            intent.putExtra("street", street);
                            intent.putExtra("city", city);
                            intent.putExtra("colony", colony);
                            intent.putExtra("state", state);
                            intent.putExtra("country",country);
                            intent.putExtra("number", number);
                            intent.putExtra("postalCode", postalCode);
                            intent.putExtra("directionComplete", directionComplete);
                            intent.putExtra("latitude", String.valueOf(currentMarkerLocation.latitude));
                            intent.putExtra("longitude", String.valueOf(currentMarkerLocation.longitude));
                            startActivity(intent);
                            finish();



                        } catch (IOException e) {
                            e.printStackTrace();
                        }






                    }
                }, 3000);

            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);
        }

        //check if gps is enabled or not and then request user to enable it
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(GetCurrentLocationActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(GetCurrentLocationActivity.this, locationSettingsResponse -> getDeviceLocation());

        task.addOnFailureListener(GetCurrentLocationActivity.this, e -> {
            if (e instanceof ResolvableApiException) {
                ResolvableApiException resolvable = (ResolvableApiException) e;
                try {
                    resolvable.startResolutionForResult(GetCurrentLocationActivity.this, 51);
                } catch (IntentSender.SendIntentException e1) {
                    e1.printStackTrace();
                }
            }
        });

        mMap.setOnMyLocationButtonClickListener(() -> false);


        mMap.setOnCameraIdleListener(this);
    }



    private void setAddressInView(String address){
        direction.setText(address);
    }


    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {


                            geocoder = new Geocoder(GetCurrentLocationActivity.this, Locale.getDefault());

                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                try {
                                    addresses = geocoder.getFromLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                    setAddressInView(addresses.get(0).getAddressLine(0));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);

                                        try {
                                            addresses = geocoder.getFromLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                            setAddressInView(addresses.get(0).getAddressLine(0));

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(GetCurrentLocationActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.current_location, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                onSearchCalled();
                return true;

            case android.R.id.home:
                goToDirections();
                return true;
            default:
                return false;
        }
    }




    public void onSearchCalled() {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).setCountry("MX")
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
            }
        } else if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getAddress());
               // Toast.makeText(GetCurrentLocationActivity.this, "ID: " + place.getId() + "address:" + place.getAddress() + "Name:" + place.getName() + " latlong: " + place.getLatLng(), Toast.LENGTH_LONG).show();
                String address = place.getAddress();

                setAddressInView(address);
                //direction.setText(place.getAddress());

                // do query with address

                LatLng latLngOfPlace = place.getLatLng();
                if (latLngOfPlace != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace, DEFAULT_ZOOM));
                }


            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(GetCurrentLocationActivity.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    @Override
    public void onCameraIdle() {
        LatLng currentMarkerLocation = mMap.getCameraPosition().target;

        new android.os.Handler().postDelayed(() -> {
            try {
                addresses = geocoder.getFromLocation(currentMarkerLocation.latitude, currentMarkerLocation.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                setAddressInView(addresses.get(0).getAddressLine(0));

            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        },500);


    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        goToDirections();
    }

    private void goToDirections(){
        new Constants().goTo(this, DirectionsActivity.class);
    }
}
