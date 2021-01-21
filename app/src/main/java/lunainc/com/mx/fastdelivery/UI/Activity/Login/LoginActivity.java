package lunainc.com.mx.fastdelivery.UI.Activity.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import lunainc.com.mx.fastdelivery.R;
import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.ResponseLRClient;
import lunainc.com.mx.fastdelivery.UI.Activity.Client.HomeActivity;
import lunainc.com.mx.fastdelivery.UI.Activity.Conductor.DriverHomeActivity;
import lunainc.com.mx.fastdelivery.UI.Activity.Socio.HomePartnerActivity;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import lunainc.com.mx.fastdelivery.customfonts.MyTextView_Roboto_Regular;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.gms.security.ProviderInstaller;


public class LoginActivity extends AppCompatActivity implements ProviderInstaller.ProviderInstallListener{

    /**
     * Variables globales
     */

    @BindView(R.id.emailInput)
    EditText emailInput;

    @BindView(R.id.passwordInput)
    EditText passwordInput;

    @BindView(R.id.resetPassword)
    TextView resetPassword;

    @BindView(R.id.createNewAccount)
    Button createNewAccount;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    private String token = "";
    private String typeAccount = "";

    private APIService apiService;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configViews();
        setContentView(R.layout.activity_login);
        ProviderInstaller.installIfNeededAsync(this, this);

        ButterKnife.bind(this);


    }

    private void configViews(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @Override
    protected void onStart() {
        super.onStart();
        initVars();

        events();

        validateInRealTime();

    }

    public void initVars(){

        context = this.getApplicationContext();
        sharedPreferences = context.getSharedPreferences(
                "credentials", Context.MODE_PRIVATE);
        token = sharedPreferences.getString(("token"), "noLogged");
        typeAccount = sharedPreferences.getString(("type_acc"), "noLogged");

        progressDialog = new ProgressDialog(this);
        apiService = ApiUtils.getAPIService();

    }


    private void events() {



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

               if (validateEmail(email)){


                   if (validatePassword(password)){




                       login(email, password);


                   }else{
                       passwordInput.setError("Se requiere una contraseña valida");
                   }


               }else{
                   emailInput.setError("Se requiere un correo electronico valido");
               }



            }
        });


        createNewAccount.setOnClickListener( v -> {
            new Constants().goTo(this, RegisterActivity.class);
        });
        
        resetPassword.setOnClickListener(v -> {
            Toast.makeText(context, "restablecer", Toast.LENGTH_SHORT).show();
        });
        
    }


    public void login(String email, String password){
        progressDialog.setTitle("Iniciando sesión");
        progressDialog.setMessage("Espere un momento...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", email)
                .addFormDataPart("password", password)
                .build();
        apiService.loginUser(requestBody).enqueue(new Callback<ResponseLRClient>() {
            @Override
            public void onResponse(@NotNull Call<ResponseLRClient> call, @NotNull Response<ResponseLRClient> response) {
                if (response.isSuccessful()){
                       if (response.body().getStatus().equals("success")){
                           saveToken(response.body().getToken(), response.body().getType_acc());
                           Toasty.success(context, response.body().getMessage(), Toasty.LENGTH_LONG, true).show();
                           goTo(response.body().getType_acc());
                       }else{
                           Toasty.warning(context, response.body().getMessage(), Toasty.LENGTH_LONG, true).show();
                       }
                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(@NotNull Call<ResponseLRClient> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toasty.warning(context, "Ocurrio un error: "+t.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        });
    }


    public void goTo(String type){
        switch (Integer.parseInt(type)){

            case 1://cliente
                goToHome();
                break;

            case 2://repartidor
                break;
            case 3://conductor
                goToHomeDriver();
                break;
            case 4://socio
                goToHomePartner();
                break;

            default:
                break;


        }
    }




    public void saveToken(String token, String type_acc){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.putString("type_acc", type_acc);
        editor.commit();
        editor.apply();
    }


    public void goToHome(){

        Intent intent = new Intent(context, HomeActivity.class);
        startActivity(intent);
        finish();

    }

    private void goToHomeDriver(){
        Intent intent = new Intent(context, DriverHomeActivity.class);
        startActivity(intent);
        finish();

    }


    public void goToHomePartner(){
        Intent intent = new Intent(context, HomePartnerActivity.class);
        startActivity(intent);
        finish();
    }



    public boolean validatePassword(String input){
        boolean aux = false;
        if (input.trim().length() >= 8){
            aux = true;
        }

        return aux;
    }


    private boolean validateEmail(String email){
       boolean aux = false;
        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true;
        }

        return aux;

    }


    public void validateInRealTime(){
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!validateEmail(charSequence.toString())){
                    emailInput.setError("Se requiere un correo electronico valido");
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(!validateEmail(editable.toString())){
                    emailInput.setError("Se requiere un correo electronico valido");
                }else{
                    emailInput.setError(null);
                }
            }
        });


        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!validatePassword(charSequence.toString())){
                    passwordInput.setError("Se requiere una contraseña valida");
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(!validatePassword(editable.toString())){
                    passwordInput.setError("Se requiere una contraseña valida");
                }else{
                    passwordInput.setError(null);
                }
            }
        });


    }

    @Override
    public void onProviderInstalled() {

    }

    @Override
    public void onProviderInstallFailed(int i, Intent intent) {

    }


}
