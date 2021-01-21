package lunainc.com.mx.fastdelivery.UI.Activity.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.com.mx.fastdelivery.R;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.ResponseLRClient;
import lunainc.com.mx.fastdelivery.Model.User;
import lunainc.com.mx.fastdelivery.UI.Activity.Client.HomeActivity;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyPhoneActivity extends AppCompatActivity {


    private String verificationId;
    private ProgressDialog progressDialog;
    @BindView(R.id.editTextCode)
    PinView editText;
    private String phoneGlobal = "";
    private User user;
    private APIService apiService;
    private String token = "";
    private Context context;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;
    @BindView(R.id.resendVerificationCodeTime)
    TextView resendVerificationCodeTime;

    @BindView(R.id.ButtonValidateAccount)
    Button ButtonValidateAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configViews();
        setContentView(R.layout.activity_verify_phone);
        ButterKnife.bind(this);
        initViews();

    }

    private void configViews(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void initViews(){
        mAuth = FirebaseAuth.getInstance();

        apiService = ApiUtils.getAPIService();
        context = this;
        sharedPreferences = context.getSharedPreferences(
                "credentials", Context.MODE_PRIVATE);
        token = new Constants().getToken(context);
        user = (User) getIntent().getSerializableExtra("user");
        progressDialog = new ProgressDialog(this);




        if (user != null){
            String phonenumber = user.getPhone();
            phoneGlobal = phonenumber;
            phonenumber = "+" + "52" + phonenumber;


            sendVerificationCode(phonenumber);

            ButtonValidateAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String code = editText.getText().toString().trim();

                    if (code.isEmpty() || code.length() < 6) {

                        editText.setError("Ingresar codigo de validación...");
                        editText.requestFocus();
                        return;
                    }
                    verifyCode(code);
                }
            });

        }

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {


                        saveData();


                    } else {
                        Toasty.error(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }



    private void sendVerificationCode(String number) {

        CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                resendVerificationCodeTime.setText("Reeenviar en (" + (millisUntilFinished/1000) + "s)");

            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
                verifyCode(code);

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.e("ErrorVerify", e.getMessage());
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void saveData() {

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_type_acc", "1")
                .addFormDataPart("name", user.getName())
                .addFormDataPart("last_name_p", user.getLast_name_p())
                .addFormDataPart("last_name_m", user.getLast_name_m())
                .addFormDataPart("email", user.getEmail())
                .addFormDataPart("password", user.getPassword())
                .addFormDataPart("phone", user.getPhone())
                .build();
        progressDialog.setTitle("Creando cuenta");
        progressDialog.setMessage("Espere un momento...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        apiService.registerUser(body).enqueue(new Callback<ResponseLRClient>() {
            @Override
            public void onResponse(@NotNull Call<ResponseLRClient> call, @NotNull Response<ResponseLRClient> response) {
                if (response.isSuccessful()){

                    if (response.body().getStatus().equals("success")){

                        saveToken(response.body().getToken(), response.body().getType_acc());
                        Toasty.success(context, response.body().getMessage()).show();
                        goToHome();

                    }else{
                    Toasty.warning(context, response.body().getMessage(), Toasty.LENGTH_LONG, true).show();

                }


            }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<ResponseLRClient> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Log.e("Error", t.getMessage());
                Toasty.warning(context, "Ocurrio un error: "+t.getMessage(), Toast.LENGTH_SHORT, true).show();

            }
        });



    }

    public void goToHome(){

        Intent intent = new Intent(context, HomeActivity.class);
        startActivity(intent);
        finish();

    }

    public void saveToken(String token, String type_acc){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.putString("type_acc", type_acc);
        editor.commit();
        editor.apply();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        new Constants().goTo(this, RegisterActivity.class);
    }
}
