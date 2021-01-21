package lunainc.com.mx.fastdelivery.Utils;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.ResponseDefault;
import lunainc.com.mx.fastdelivery.R;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackerService extends Service {

    private static final String TAG = TrackerService.class.getSimpleName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private APIService apiService;



    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    public void onCreate() {
        super.onCreate();
        apiService = ApiUtils.getAPIService();
        buildNotification();
        loginToFirebase();
    }

    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_check_white_24dp);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "fastappnotification";
            CharSequence name = "fastappnotification";
            String description = "Channel description";


            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setShowBadge(false);
            Objects.requireNonNull(notificationManager).createNotificationChannel(mChannel);
        }


        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "fastappnotification")
                .setContentTitle(getString(R.string.app_name))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_check_white_24dp)
                .setLargeIcon(bitmap)
                .setOngoing(true)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentText(getString(R.string.notification_text))
                .setContentIntent(broadcastIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));



        startForeground(1, notification.build());
    }


    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received stop broadcast");
            // Stop the service when the notification is tapped
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };


    private void loginToFirebase() {
        // Authenticate with Firebase, and request location updates
        String email = getString(R.string.firebase_email);
        String password = getString(R.string.firebase_password);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "firebase auth success");
                        requestLocationUpdates();
                    } else {
                        Log.d(TAG, "firebase auth failed");
                    }
                });
    }


    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        int FOUR_MINUTES = 1000 * 60 * 4;
        request.setInterval(FOUR_MINUTES);
        //request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        Log.d(TAG, "location update " + location);
                        String id = new Constants().getIDUser(TrackerService.this);

                        RequestBody body = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("latitude", String.valueOf(location.getLongitude()))
                                .addFormDataPart("longitude", String.valueOf(location.getLongitude()))
                                .build();

                        apiService.updateLocationDriver("Accept", new Constants().getToken(TrackerService.this), body)
                        .enqueue(new Callback<ResponseDefault>() {
                            @Override
                            public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {

                                if (response.isSuccessful()){
                                    if (response.body().getStatus().equals("success")){
                                        Log.e("Succes", response.body().getMessage());
                                    }else{
                                        Toasty.error(TrackerService.this, response.body().getMessage()).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {
                                Log.e("Error",t.getMessage());
                            }
                        });

                    }
                }
            }, null);
        }
    }


}
