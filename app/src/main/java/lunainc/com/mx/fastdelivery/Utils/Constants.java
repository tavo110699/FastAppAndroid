package lunainc.com.mx.fastdelivery.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Model.ResponseDefault;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Constants {


    public String CURRENT_URL = "http://192.168.1.76:8080";
    private SharedPreferences sharedPref;


    public void updateTokenFromNew(APIService mAPIService,String deviceToken, String token){
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("device_token", deviceToken)
                .build();
        mAPIService.updateToken("Accept", token, requestBody).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {
                if (response.isSuccessful()){
                    Log.e("ErrorRed", Objects.requireNonNull(response.body()).getMessage());
                }else{
                    Log.e("ErrorRed", "Error(500) en el servidor");
                    //Toast.makeText(MainActivity.this, "Error(500) en el servidor", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {
                Log.e("ErrorRed", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    public void updateDeviceToken(APIService mAPIService, String completeToken){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String deviceToken = instanceIdResult.getToken();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("device_token", deviceToken)
                    .build();
            mAPIService.updateToken("Accept", completeToken, requestBody).enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {
                    if (response.isSuccessful()){
                        Log.e("ErrorRed", Objects.requireNonNull(response.body()).getMessage());
                    }else{
                        Log.e("ErrorRed", "Error(500) en el servidor");
                        //Toast.makeText(MainActivity.this, "Error(500) en el servidor", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {
                    Log.e("ErrorRed", Objects.requireNonNull(t.getMessage()));
                }
            });
        });
    }


    public String getToken(Context context){
        sharedPref = context.getSharedPreferences(
                "credentials", Context.MODE_PRIVATE);

        return "Bearer " +sharedPref.getString(("token"), "noLogged");
    }

    public String getIDUser(Context context){
        sharedPref = context.getSharedPreferences(
                "credentials", Context.MODE_PRIVATE);

        return sharedPref.getString(("id"), "noLogged");
    }

    public String getUsername(Context context){
        sharedPref = context.getSharedPreferences(
                "credentials", Context.MODE_PRIVATE);

        return sharedPref.getString(("username"), "noLogged");
    }

    public void goTo(Activity activity, Class clase){
        Intent intent = new Intent(activity, clase);
        activity.startActivity(intent);
        activity.finish();
    }

    public void goToActivity(Activity activity, Class clase){
        Intent intent = new Intent(activity, clase);
        activity.startActivity(intent);
    }

}
