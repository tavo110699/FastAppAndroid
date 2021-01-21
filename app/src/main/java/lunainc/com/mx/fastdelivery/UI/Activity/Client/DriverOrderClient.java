package lunainc.com.mx.fastdelivery.UI.Activity.Client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.Driver;
import lunainc.com.mx.fastdelivery.Model.MessageEventResponseDriverToClient;
import lunainc.com.mx.fastdelivery.Model.OrderDriver;
import lunainc.com.mx.fastdelivery.Model.ResponseDefault;
import lunainc.com.mx.fastdelivery.R;import com.skyfishjy.library.RippleBackground;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import lunainc.com.mx.fastdelivery.customfonts.MyTextView_Roboto_Regular;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DriverOrderClient extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener{


    private static final int AUTOCOMPLETE_REQUEST_CODE = 22;
    private static final String TAG = DriverOrderClient.class.getSimpleName();


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

    @BindView(R.id.ripple_bg)
    RippleBackground rippleBg;

    @BindView(R.id.textHello)
    TextView textHello;

    @BindView(R.id.picker)
    ImageView picker;

    @BindView(R.id.init_location)
    MyTextView_Roboto_Regular init_location;

    @BindView(R.id.init_locationTop)
    MyTextView_Roboto_Regular init_locationTop;

    @BindView(R.id.finish_location)
    MyTextView_Roboto_Regular finishLocation;

    @BindView(R.id.finish_locationTop)
    MyTextView_Roboto_Regular finish_locationTop;

    @BindView(R.id.actionBtn)
    ExtendedFloatingActionButton actionBtn;


    @BindView(R.id.containerDialog)
    MaterialCardView containerDialog;

    @BindView(R.id.containerTop)
    MaterialCardView containerTop;

    @BindView(R.id.containerBottom)
    MaterialCardView containerBottom;

    @BindView(R.id.containerInfoDriver)
    MaterialCardView containerInfoDriver;

    @BindView(R.id.imageDriver)
    CircleImageView imageDriver;

    @BindView(R.id.nameDriver)
    TextView nameDriver;

    @BindView(R.id.model)
    TextView model;

    @BindView(R.id.placas)
    TextView placas;

    @BindView(R.id.searchAnother)
    Button searchAnother;

    @BindView(R.id.acceptDriver)
    Button acceptDriver;

    @BindView(R.id.finishRide)
    Button finishRide;

    @BindView(R.id.calif)
    Chip calif;

    @BindView(R.id.actions)
    LinearLayout actions;

    @BindView(R.id.actionFinish)
    LinearLayout actionFinish;


    @BindView(R.id.titleDialog)
    TextView titleDialog;

    @BindView(R.id.descDialog)
    TextView descDialog;

    @BindView(R.id.actionNo)
    Button actionNo;

    @BindView(R.id.actionOk)
    Button actionOk;

    private View mapView;

    private boolean activateMoveCamera = false;

    private final float DEFAULT_ZOOM = 18;

    private ProgressDialog progressDialog;
    private Location locationResultDest;

    private APIService apiService;
    private String token;
    private Context context;
    private ProgressDialog progressSearchDriver;

    private boolean auxAlert = true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_order_client);
        ButterKnife.bind(this);
        if (ActivityCompat.checkSelfPermission(DriverOrderClient.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(DriverOrderClient.this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            Toast.makeText(this, "Para usar esta función necesitamos acceder a tu ubicación", Toast.LENGTH_SHORT).show();
        }
        configViews();
        initVars();



    }

    private void initVars(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();
        context = this;
        token = new Constants().getToken(context);
        apiService = ApiUtils.getAPIService();
        progressSearchDriver = new ProgressDialog(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(DriverOrderClient.this);
        Places.initialize(DriverOrderClient.this, getString(R.string.MAP_VIEW_BUNDLE_KEY));

    }

    private void configViews(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        requestPermission();
        setDataInView();
        events();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @SuppressLint("SetTextI18n")
    private void setDataInView() {

        containerInfoDriver.setVisibility(View.GONE);
        containerBottom.setVisibility(View.VISIBLE);
        String date = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
        int time = Integer.parseInt(date);

        if(time >= 0 && time < 12){
            textHello.setText("Buenos días, "+new Constants().getUsername(this));

        }else if(time >= 12 && time < 18){
            textHello.setText("Buenas tardes, "+new Constants().getUsername(this));
        }else if(time >= 18 && time < 24){
            textHello.setText("Buenas noches, "+new Constants().getUsername(this));

        }


    }

    private void setDataInitLocationView(String address) {
        init_location.setText(address);
        init_locationTop.setText(address);
        init_location.setTextSize(15);
        init_locationTop.setTextSize(15);
    }

    private void events() {

        actionBtn.setOnClickListener(v -> {
            LatLng currentMarkerLocation = mMap.getCameraPosition().target;
            rippleBg.startRippleAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rippleBg.stopRippleAnimation();
                    progressSearchDriver.setTitle("Buscando unidad");
                    progressSearchDriver.setMessage("Espere un momento...");
                    progressSearchDriver.setCancelable(false);
                    progressSearchDriver.show();

                    try {
                        addresses = geocoder.getFromLocation(currentMarkerLocation.latitude, currentMarkerLocation.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        setAddressInView(addresses.get(0).getAddressLine(0));


                        String directionComplete = addresses.get(0).getAddressLine(0);


                        if (locationResultDest != null){
                            RequestBody body = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("latitude_start", String.valueOf(locationResultDest.getLatitude()))
                                    .addFormDataPart("longitude_start", String.valueOf(locationResultDest.getLongitude()))
                                    .addFormDataPart("latitude_finish", String.valueOf(currentMarkerLocation.latitude))
                                    .addFormDataPart("longitude_finish", String.valueOf(currentMarkerLocation.longitude))
                                    .addFormDataPart("direction_complete", directionComplete)
                                    .build();


                            apiService.createOrderDriver("Accept", token, body).enqueue(new Callback<OrderDriver>() {
                                @Override
                                public void onResponse(@NotNull Call<OrderDriver> call, @NotNull Response<OrderDriver> response) {

                                    if (response.isSuccessful()){
                                        if (response.body().getStatus().equals("success")){

                                            Toasty.success(context, response.body().getMessage()).show();
                                            actionBtn.setVisibility(View.GONE);
                                            searchDriver(response.body().getId());


                                        }else{
                                            actionBtn.setVisibility(View.VISIBLE);
                                            Toasty.error(context, "Error al crear orden").show();
                                        }
                                    }else{
                                        Log.e("Error", response.message());

                                    }


                                }

                                @Override
                                public void onFailure(@NotNull Call<OrderDriver> call, @NotNull Throwable t) {
                                        Log.e("Error", t.getMessage());
                                }
                            });
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }






                }
            }, 3000);

        });

        finishLocation.setOnClickListener(v -> {
            onSearchCalled();
        });


    }

    private void searchDriver(String order_id){

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_driver_order", order_id)
                .build();


        apiService.searchDriverToOrder("Accept", token, body).enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(@NotNull Call<Driver> call, @NotNull Response<Driver> response) {

                if (response.isSuccessful()){

                    if (response.body().getResp_status().equals("success")){
                        //Toasty.success(context, response.body().getMessage()).show();

                        showInUI(response.body(), order_id);

                    }else {//error


                        if (response.body().getMessage().equals("No hay unidades disponibles")){


                            goneVisibilityAll();
                            containerDialog.setVisibility(View.VISIBLE);

                            titleDialog.setText(response.body().getMessage());
                            descDialog.setText("¿Desea volver a buscar?");

                            actionOk.setOnClickListener(v -> {
                                progressSearchDriver.show();
                                searchDriver(order_id);
                            });

                            actionNo.setOnClickListener(v -> {
                                goneVisibilityAll();
                                actionBtn.setVisibility(View.VISIBLE);
                                containerTop.setVisibility(View.VISIBLE);
                            });



                        }else if(response.body().getMessage().equals("Es posible que esta orden no exista")){

                            goneVisibilityAll();
                            actionBtn.setVisibility(View.VISIBLE);
                            containerTop.setVisibility(View.VISIBLE);

                        }


                    }

                }

                progressSearchDriver.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<Driver> call, @NotNull Throwable t) {
                progressSearchDriver.dismiss();
                Log.e("error", t.getMessage());
            }
        });




    }

    @SuppressLint("SetTextI18n")
    private void showInUI(Driver driver, String order_id){
        goneVisibilityAll();
        containerInfoDriver.setVisibility(View.VISIBLE);



        Glide.with(this).load(new Constants().CURRENT_URL+"/photos/"+driver.getUser().getImage()).into(imageDriver);
        nameDriver.setText(driver.getUser().getName());
        model.setText("Modelo "+driver.getModelCar() + ", Número"+driver.getNo_unidad());
        placas.setText("No. placas: "+driver.getPlacas()+", Telefono: "+driver.getUser().getPhone());
        
        searchAnother.setOnClickListener(v -> {
            goneVisibilityAll();
            searchAnotherDriver(order_id, driver.getId_user());

        });
        acceptDriver.setOnClickListener(v -> {

            acceptDriverRide(driver.getId_user(), order_id);


        });

    }


    private void acceptDriverRide(String driver_id, String order_id){

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("driver_id", driver_id)
                .addFormDataPart("order_id", order_id)
                .addFormDataPart("direction", "Origen: "+init_locationTop.getText().toString()+" - Destino "+finish_locationTop.getText().toString())
                .build();

        goneVisibilityAll();
        toolbar.setTitle("Esperando respuesta del conductor");

        apiService.acceptDriverByClient("Accept", token, body).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {

                if (response.isSuccessful()){

                    if (response.body().getStatus().equals("success")){
                        Toasty.success(context, "Por seguridad, espera que el conductor acepte el viaje").show();
                        int FIVE_MINUTES = 1000 * 60 * 5;

                        new Handler().postDelayed(() -> {
                            askActionContinue(order_id, driver_id);

                        },FIVE_MINUTES);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEventResponseDriverToClient event) {
        auxAlert = false;

        if (event.getStatus().equals("success")){
            goneVisibilityAll();
            toolbar.setTitle("Fast App");
            Toasty.success(context, event.getMessage()).show();

            actions.setVisibility(View.GONE);
            actionFinish.setVisibility(View.VISIBLE);
            containerInfoDriver.setVisibility(View.VISIBLE);

            finishRide.setOnClickListener(v -> {
                //goneVisibilityAll();

                RequestBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("driver_id", event.getId_driver())
                        .addFormDataPart("order_id", event.getId_order())
                        .build();

                apiService.finishRideByClient("Accept", token, body).enqueue(new Callback<ResponseDefault>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {
                        if (response.isSuccessful()){

                            if (response.body().getStatus().equals("success")){

                                new Constants().goTo(DriverOrderClient.this, HomeActivity.class);

                            }


                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });



        });

        }else {
            goneVisibilityAll();
            //containerTop.setVisibility(View.VISIBLE);

            containerDialog.setVisibility(View.VISIBLE);

            titleDialog.setText("Buscar otro");
            descDialog.setText("¿Desea buscar otro vehiculo?");

            actionOk.setOnClickListener(v -> {
                goneVisibilityAll();
                toolbar.setTitle("Fast App");
                searchAnotherDriver(event.getId_order(), event.getId_driver());
                activateMoveCamera = false;
            });

            actionNo.setOnClickListener(v -> {
                goneVisibilityAll();
                actionBtn.setVisibility(View.VISIBLE);
                containerTop.setVisibility(View.VISIBLE);
                activateMoveCamera = true;

            });



        }


       /*
        if (event.getStatus().equals("success")){

            toolbar.setTitle("Fast App");
            Toasty.success(context, event.getMessage()).show();
            actionFinish.setVisibility(View.VISIBLE);
            finishRide.setOnClickListener(v -> {



                containerInfoDriver.setVisibility(View.GONE);
                actionFinish.setVisibility(View.GONE);
                containerTop.setVisibility(View.GONE);
                containerBottom.setVisibility(View.VISIBLE);



        }else{

            Toast.makeText(context, "doo", Toast.LENGTH_SHORT).show();

            searchAnotherDriver(event.getId_order(), event.getId_driver());
            actions.setVisibility(View.VISIBLE);
            toolbar.setTitle("Fast App");
        }
        */


    }


    private void goneVisibilityAll(){
        containerInfoDriver.setVisibility(View.GONE);
        actionFinish.setVisibility(View.GONE);
        containerTop.setVisibility(View.GONE);
        containerBottom.setVisibility(View.GONE);
        containerDialog.setVisibility(View.GONE);
        actionBtn.setVisibility(View.GONE);
    }


    private void askActionContinue(String order_id, String driver_id){

        if (auxAlert) {

            goneVisibilityAll();
            //containerTop.setVisibility(View.VISIBLE);

            containerDialog.setVisibility(View.VISIBLE);

            titleDialog.setText("Tu transporte aún no responde");
            descDialog.setText("¿Deseas esperar más tiempo su respuesta?");

            actionOk.setOnClickListener(v -> {
                int FIVE_MINUTES = 1000 * 60 * 5;

                new Handler().postDelayed(() -> {
                    askActionContinue(order_id, driver_id);

                }, FIVE_MINUTES);
            });

            actionNo.setOnClickListener(v -> {
                goneVisibilityAll();
                searchAnotherDriver(order_id, driver_id);
                actions.setVisibility(View.VISIBLE);
                toolbar.setTitle("Fast App");
                //cancel order o discart driver

            });




        }

    }


    private void searchAnotherDriver(String order_id, String driver_id){
        Toasty.info(context, "Buscando nuevo tranporte").show();

       goneVisibilityAll();
        progressSearchDriver.show();
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_driver_order", order_id)
                .addFormDataPart("blacklist", driver_id)
                .build();
        apiService.searchDriverAnotherToOrder("Accept", token, body).enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(@NotNull Call<Driver> call, @NotNull Response<Driver> response) {
                if (response.isSuccessful()){

                    if (response.body().getResp_status().equals("success")){
                        containerInfoDriver.setVisibility(View.VISIBLE);
                        Toasty.success(context, response.body().getMessage()).show();

                        showInUI(response.body(), order_id);
                        Log.e("success", "sucess");

                    }else {

                        if (response.body().getMessage().equals("No hay unidades disponibles")){

                            Log.e("success", "no hay");
                            goneVisibilityAll();
                            //containerTop.setVisibility(View.VISIBLE);

                            containerDialog.setVisibility(View.VISIBLE);

                            titleDialog.setText(response.body().getMessage());
                            descDialog.setText("¿Desea volver a buscar?");

                            actionOk.setOnClickListener(v -> {
                                progressSearchDriver.show();
                                searchAnotherDriver(order_id, driver_id);
                            });

                            actionNo.setOnClickListener(v -> {
                                goneVisibilityAll();
                                actionBtn.setVisibility(View.VISIBLE);
                                containerTop.setVisibility(View.VISIBLE);

                            });





                        }else if(response.body().getMessage().equals("Es posible que esta orden no exista")){
                            Log.e("success", "no existe");
                            goneVisibilityAll();
                            actionBtn.setVisibility(View.VISIBLE);
                            containerTop.setVisibility(View.VISIBLE);

                        }
                    }

                }
                progressSearchDriver.hide();
            }

            @Override
            public void onFailure(@NotNull Call<Driver> call, @NotNull Throwable t) {

            }
        });
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(DriverOrderClient.this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(DriverOrderClient.this, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

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

            SettingsClient settingsClient = LocationServices.getSettingsClient(DriverOrderClient.this);
            Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

            task.addOnSuccessListener(DriverOrderClient.this, locationSettingsResponse -> getDeviceLocation());

            task.addOnFailureListener(DriverOrderClient.this, e -> {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(DriverOrderClient.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            mMap.setOnMyLocationButtonClickListener(() -> false);


            mMap.setOnCameraIdleListener(this);

        }else {
            requestPermission();
        }
    }



    private void setAddressInView(String address){
        direction.setText(address);
        finishLocation.setText(address);
        finish_locationTop.setText(address);
        finishLocation.setTextSize(15);
        finish_locationTop.setTextSize(15);
        picker.setVisibility(View.VISIBLE);
        containerBottom.setVisibility(View.GONE);
        containerTop.setVisibility(View.VISIBLE);

    }


    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {


                            geocoder = new Geocoder(DriverOrderClient.this, Locale.getDefault());

                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                try {
                                    addresses = geocoder.getFromLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                    setDataInitLocationView(addresses.get(0).getThoroughfare());
                                    locationResultDest = mLastKnownLocation;
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
                                            setDataInitLocationView(addresses.get(0).getThoroughfare());
                                            locationResultDest = mLastKnownLocation;

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(DriverOrderClient.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (activateMoveCamera){
                dialogExitView();
            }else {
                goToDirections();
            }
            return true;
        }
        return false;
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
                //Toast.makeText(DriverOrderClient.this, "ID: " + place.getId() + "address:" + place.getAddress() + "Name:" + place.getName() + " latlong: " + place.getLatLng(), Toast.LENGTH_LONG).show();
                String address = place.getName();//;addresses.get(0).getThoroughfare()

                setAddressInView(address);
                activateMoveCamera = true;
                actionBtn.setVisibility(View.VISIBLE);
                //direction.setText(place.getAddress());

                // do query with address

                LatLng latLngOfPlace = place.getLatLng();
                if (latLngOfPlace != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace, DEFAULT_ZOOM));
                }


            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(DriverOrderClient.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    @Override
    public void onCameraIdle() {

        if (activateMoveCamera){
            LatLng currentMarkerLocation = mMap.getCameraPosition().target;

            new android.os.Handler().postDelayed(() -> {
                try {
                    addresses = geocoder.getFromLocation(currentMarkerLocation.latitude, currentMarkerLocation.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    setAddressInView(addresses.get(0).getThoroughfare());

                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                }
            },500);

        }


    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (activateMoveCamera){
            dialogExitView();
        }else {
            goToDirections();
        }


    }



    private void dialogExitView(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("¿Desea salir de esta pantalla?");
        alert.setMessage("Podría perder su carrera, de hacer esta cción");
        alert.setCancelable(false);
        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Constants().goTo(DriverOrderClient.this, HomeActivity.class);

            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }


    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, 1);
    }

    private void goToDirections(){
        new Constants().goTo(this, HomeActivity.class);
    }
}
