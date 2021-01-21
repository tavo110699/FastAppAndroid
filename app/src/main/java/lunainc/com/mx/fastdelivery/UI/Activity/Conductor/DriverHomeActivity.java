package lunainc.com.mx.fastdelivery.UI.Activity.Conductor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.request.DirectionRequestParam;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.bumptech.glide.Glide;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.gms.common.api.ResolvableApiException;
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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import io.reactivex.internal.operators.flowable.FlowableObserveOn;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.MessageEventDriver;
import lunainc.com.mx.fastdelivery.Model.OrderDriver;
import lunainc.com.mx.fastdelivery.Model.ResponseDefault;
import lunainc.com.mx.fastdelivery.Model.User;
import lunainc.com.mx.fastdelivery.R;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import lunainc.com.mx.fastdelivery.Utils.TrackerService;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class DriverHomeActivity extends AppCompatActivity implements OnMapReadyCallback {


    private static final String TAG = DriverHomeActivity.class.getSimpleName();


    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Location mLastKnownLocation;
    private LocationCallback locationCallback;

    private Geocoder geocoder;
    private List<Address> addresses;

    private View mapView;

    private boolean activateMoveCamera = false;

    private final float DEFAULT_ZOOM = 18;

    private static final int PERMISSIONS_REQUEST = 1;

    private String token = "";
    private APIService apiService;
    private SharedPreferences sharedPref;
    private Context context;
    private User userG;
    private OrderDriver orderDriver;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.containerInfoClient)
    MaterialCardView containerInfoClient;

    @BindView(R.id.imageClient)
    CircleImageView imageClient;

    @BindView(R.id.nameClient)
    TextView nameClient;

    @BindView(R.id.direction)
    TextView direction;

    @BindView(R.id.rejectClient)
    Button rejectClient;

    @BindView(R.id.acceptClient)
    Button acceptClient;

    @BindView(R.id.finishRide)
    Button finishRide;

    @BindView(R.id.driverArrive)
    Button driverArrive;

    @BindView(R.id.actions)
    LinearLayout actions;

    @BindView(R.id.actionArrived)
    LinearLayout actionArrived;

    @BindView(R.id.actionFinish)
    LinearLayout actionFinish;

    @BindView(R.id.switchStatus)
    LabeledSwitch switchStatus;

    @BindView(R.id.availableCredits)
    TextView availableCredits;

    @BindView(R.id.imageDriver)
    CircleImageView imageDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);
        ButterKnife.bind(this);
        initVars();

        if (ActivityCompat.checkSelfPermission(DriverHomeActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(DriverHomeActivity.this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            Toast.makeText(this, "Para usar esta función necesitamos acceder a tu ubicación", Toast.LENGTH_SHORT).show();
        }

        getData();
        initLocation();

    }

    private void initVars(){
        context = this;
        sharedPref = context.getSharedPreferences(
                "credentials", Context.MODE_PRIVATE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(DriverHomeActivity.this);
        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(DriverHomeActivity.this);
        Places.initialize(DriverHomeActivity.this, getString(R.string.MAP_VIEW_BUNDLE_KEY));

        apiService = ApiUtils.getAPIService();
        token = "Bearer " + sharedPref.getString(("token"), "noLogged");

        updateStatusDriver(true);
        new Constants().updateDeviceToken(apiService, token);

    }

    private void getData(){

        apiService.getUserDataDriver("Accept", token).enqueue(new Callback<User>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {

                if(response.isSuccessful()){
                    User user = response.body();
                    userG = user;

                    try{

                        if (user.getDriver().getStatus().equals("offline")){
                            switchStatus.setOn(false);

                            //switchStatus.set("No disponible");
                        }
                        switchStatus.setOnToggledListener(new OnToggledListener() {
                            @Override
                            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                                updateStatusDriver(isOn);
                            }
                        });



                        availableCredits.setText("Creditos: "+user.getDriver().getCredits());
                        Glide.with(context).load(new Constants().CURRENT_URL+"/photos/"+user.getDriver().getUser().getImage()).into(imageDriver);

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("id", user.getId());
                        editor.commit();
                        editor.apply();
                    }catch (NullPointerException nulP){
                        Log.e("Error", nulP.getMessage());
                    }






                }

            }

            @Override
            public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {

            }
        });

    }


    private void updateStatusDriver(boolean status){
        String valueSend = "offline";
        if (status){
            valueSend = "online";
        }

        RequestBody bodyUpdate = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("status", valueSend)
                .build();

        apiService.updateStatusDriver("Accept", token, bodyUpdate).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {

                if (response.isSuccessful()){

                    if (response.body().getStatus().equals("success")){
                        Toasty.success(context, response.body().getMessage()).show();

                    }else{
                        Toasty.error(context, response.body().getMessage()).show();
                    }

                }else {
                    Toasty.error(context, response.message()).show();
                }

            }

            @Override
            public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {
                Toasty.error(context, "Error: "+t.getMessage()).show();
            }
        });

    }


    private void initLocation(){

        // Check GPS is enabled
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        assert lm != null;
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Por favor habilita los servicion de ubicación", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(DriverHomeActivity.this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(DriverHomeActivity.this, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mMap = googleMap;

            MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.styles);
            mMap.setMapStyle(mapStyleOptions);
            mMap.setIndoorEnabled(false);

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                layoutParams.setMargins(0, 50, 50, 180);
            }

            //check if gps is enabled or not and then request user to enable it
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(5000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

            SettingsClient settingsClient = LocationServices.getSettingsClient(DriverHomeActivity.this);
            Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

            task.addOnSuccessListener(DriverHomeActivity.this, locationSettingsResponse -> getDeviceLocation());

            task.addOnFailureListener(DriverHomeActivity.this, e -> {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(DriverHomeActivity.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            mMap.setOnMyLocationButtonClickListener(() -> false);



        }else {
            requestPermission();
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {


                            geocoder = new Geocoder(DriverHomeActivity.this, Locale.getDefault());

                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                try {
                                    addresses = geocoder.getFromLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                   // setDataInitLocationView(addresses.get(0).getThoroughfare());
                                   // locationResultDest = mLastKnownLocation;
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
                                          //  setDataInitLocationView(addresses.get(0).getThoroughfare());
                                           // locationResultDest = mLastKnownLocation;
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                            }
                        } else {
                            Toast.makeText(DriverHomeActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEventDriver event) {


        containerInfoClient.setVisibility(View.VISIBLE);
        actions.setVisibility(View.VISIBLE);
        actionFinish.setVisibility(View.GONE);
        Toasty.info(context, "Nueva solicitud de transporte").show();

        direction.setText(event.getDirection());

        nameClient.setText(event.getMessage());
        acceptClient.setOnClickListener(v-> {

            rejectRequestRide(event.getId_order(), event.getId_client(), "success");
            int credits = Integer.parseInt(userG.getDriver().getCredits()) - 1;
            availableCredits.setText("Creditos: "+credits);

            RequestBody bodyOrder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("order_id", event.getId_order())
                    .build();

            apiService.orderDataRide("Accept", token, bodyOrder).enqueue(new Callback<OrderDriver>() {
                @Override
                public void onResponse(@NotNull Call<OrderDriver> call, @NotNull Response<OrderDriver> response) {
                    if (response.isSuccessful()){

                        if (response.body().getStatus().equals("success")){
                            orderDriver = response.body();

                            actions.setVisibility(View.GONE);
                            actionArrived.setVisibility(View.VISIBLE);
                            if (orderDriver != null){
                                //Trazar ruta para ir por el cliente
                                String originLat = String.valueOf(mLastKnownLocation.getLatitude());
                                String originLong = String.valueOf(mLastKnownLocation.getLongitude());
                                String destLat = orderDriver.getLatitude_start();
                                String destLong = orderDriver.getLatitude_start();
                                drawRouteTo(originLat, originLong, destLat, destLong);
                            }

                            driverArrive.setOnClickListener(vi ->{
                                mMap.clear();
                                actionArrived.setVisibility(View.GONE);
                                actionFinish.setVisibility(View.VISIBLE);

                            });


                        }else {
                            Toasty.error(context, "Error al buscar la orden").show();
                        }


                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderDriver> call, Throwable t) {

                }
            });






        });

        rejectClient.setOnClickListener(v->{
            containerInfoClient.setVisibility(View.GONE);
            rejectRequestRide(event.getId_order(), event.getId_client(), "error");

        });


        finishRide.setOnClickListener(v->{
            containerInfoClient.setVisibility(View.GONE);
            actionFinish.setVisibility(View.GONE);
        });


    }



    private void drawRouteTo(String originLat, String originLong, String destLat, String destLog ){

        GoogleDirection.withServerKey(getResources().getString(R.string.MAP_VIEW_BUNDLE_KEY))
                .from(new LatLng(Float.parseFloat(originLat), Float.parseFloat(originLong)))
                .to(new LatLng(Float.parseFloat(destLat), Float.parseFloat(destLog)))
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction) {
                        if(direction.isOK()) {
                            // Do something
                            addMarkersToMap(direction, originLat, originLong, destLat, destLog);
                            Log.e("Error", "Funciona");

                        } else {
                            // Do something
                            Log.e("Error", "Direccion");
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });
    }

    private Polyline addMarkersToMap(Direction direction, String originLat, String originLong, String destLat, String destLog){
        mMap.addMarker(new MarkerOptions().position(new LatLng(Float.parseFloat(originLat), Float.parseFloat(originLong))));
        mMap.addMarker(new MarkerOptions().position(new LatLng(Float.parseFloat(destLat), Float.parseFloat(destLog))));

        ArrayList<LatLng> d = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
        Polyline line = mMap.addPolyline(new PolylineOptions().clickable(false).add(d.get(0)));

        return line;
    }

    private void rejectRequestRide(String order_id, String client_id, String status){
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("order_id", order_id)
                .addFormDataPart("client_id", client_id)
                .addFormDataPart("status", status)
                .build();

        apiService.requestRide("Accept", token, body).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {

                if (response.isSuccessful()){

                    if (response.body().getStatus().equals("success")){
                        Toasty.success(context, response.body().getMessage()).show();





                    }else{
                        Toasty.error(context, response.body().getMessage()).show();
                    }

                }

            }

            @Override
            public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });

    }



    private void startTrackerService() {
        startService(new Intent(this, TrackerService.class));
       // finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
            startTrackerService();
        } else {
            finish();
        }
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
            }


        }
    }


}
