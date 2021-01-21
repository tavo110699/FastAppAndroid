package lunainc.com.mx.fastdelivery.UI.Activity.Client.Directions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import lunainc.com.mx.fastdelivery.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.ResponseDefault;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateDirectionActivity extends AppCompatActivity  {




    @BindView(R.id.streetDirection)
    TextInputEditText streetDirection;

    @BindView(R.id.numberExteriorDirection)
    TextInputEditText numberExteriorDirection;

    @BindView(R.id.numberInteriorDirection)
    TextInputEditText numberInteriorDirection;

    @BindView(R.id.referencesDirection)
    TextInputEditText referencesDirection;

    @BindView(R.id.cpDirection)
    TextInputEditText cpDirection;

    @BindView(R.id.colonyDirection)
    TextInputEditText colonyDirection;

    @BindView(R.id.cityDirection)
    TextInputEditText cityDirection;

    @BindView(R.id.stateDirection)
    TextInputEditText stateDirection;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btnAction)
    ExtendedFloatingActionButton btnAction;


    private ProgressDialog progressDialog;

    private String street = "";
    private String city = "";
    private String colony = "";
    private String state = "";
    private String country = "";
    private String number = "";
    private String postalCode = "";
    private String directionComplete = "";
    private String latitude = "";
    private String longitude = "";

    private APIService apiService;
    private String token = "";
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_direction);
        ButterKnife.bind(this);

        getValuesFromIntent();
        configViews();
        initVars();

    }


    private void getValuesFromIntent(){
        street = getIntent().getStringExtra("street");
        city = getIntent().getStringExtra("city");
        colony = getIntent().getStringExtra("colony");
        state = getIntent().getStringExtra("state");
        country = getIntent().getStringExtra("country");
        number = getIntent().getStringExtra("number");
        postalCode = getIntent().getStringExtra("postalCode");
        directionComplete = getIntent().getStringExtra("directionComplete");
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
    }

    private void configViews(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void initVars(){
        context = this;
        apiService = ApiUtils.getAPIService();
        token = new Constants().getToken(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Espere un momento");
        progressDialog.setMessage("Guardando dirección...");
        progressDialog.setCancelable(false);

    }


    @Override
    protected void onStart() {
        super.onStart();
        events();
        setFullDataInEdit(street, number, postalCode, colony, city, state);

    }

    private void events() {

        btnAction.setOnClickListener(v -> saveDirection());

    }



    private void saveDirection(){
        String calle = Objects.requireNonNull(streetDirection.getText()).toString();
        String numInterior = Objects.requireNonNull(numberInteriorDirection.getText()).toString();
        String numExterior = Objects.requireNonNull(numberExteriorDirection.getText()).toString();
        String referencias = Objects.requireNonNull(referencesDirection.getText()).toString();
        String cp = Objects.requireNonNull(cpDirection.getText()).toString();
        String colonia = Objects.requireNonNull(colonyDirection.getText()).toString();
        String ciudad = Objects.requireNonNull(cityDirection.getText()).toString();
        String estado = Objects.requireNonNull(stateDirection.getText()).toString();

        if ( (validateSize(stateDirection, calle)) &&
                (validateSizeNumber(numberExteriorDirection, numExterior)) &&
                (validateSize(referencesDirection, referencias)) &&
                (validateSize(cpDirection, cp)) &&
                (validateSize(colonyDirection, colonia)) &&
                (validateSize(cityDirection, ciudad)) &&
                (validateSize(stateDirection, estado))
        ){
            if (numInterior.length() <= 0){
                numInterior = "-";
            }
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("interior_number", numInterior)
                    .addFormDataPart("exterior_number", numExterior)
                    .addFormDataPart("street", calle)
                    .addFormDataPart("colony", colonia)
                    .addFormDataPart("city", ciudad)
                    .addFormDataPart("state", estado)
                    .addFormDataPart("country", country)
                    .addFormDataPart("postal_code", cp)
                    .addFormDataPart("latitude", latitude)
                    .addFormDataPart("longitude", longitude)
                    .addFormDataPart("direction_complete", directionComplete)
                    .build();

            progressDialog.show();

            apiService.createDirection("Accept", token, body).enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {

                    if (response.isSuccessful()){

                        assert response.body() != null;
                        if (response.body().getStatus().equals("success")){
                            Toasty.success(CreateDirectionActivity.this, response.body().getMessage()).show();
                            new Constants().goTo(CreateDirectionActivity.this, DirectionsActivity.class);

                        }else{
                            Toasty.error(CreateDirectionActivity.this, response.body().getMessage()).show();

                        }


                    }


                }

                @Override
                public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {
                    Toasty.error(CreateDirectionActivity.this, Objects.requireNonNull(t.getMessage())).show();

                }
            });





            progressDialog.dismiss();
        }else {
            Toasty.warning(this, "Debes llenar correctamente los campos").show();
        }




    }


    private boolean validateSize(TextInputEditText textInputEditText, String value){

        if (value.trim().length() > 4){
            return true;
        }else {
            textInputEditText.setError("Este campo es requerido");
            return false;
        }



    }


    private boolean validateSizeNumber(TextInputEditText textInputEditText, String value){

        if (value.trim().length() >= 1){
            return true;
        }else {
            textInputEditText.setError("Este campo es requerido");
            return false;
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            goToGetCurrentDirection();
            return true;
        }
        return false;
    }



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        goToGetCurrentDirection();
    }

    private void goToGetCurrentDirection(){
        new Constants().goTo(this, GetCurrentLocationActivity.class);
    }









    private void setFullDataInEdit(String calle, String number, String cp, String colonia, String ciudad, String estado){
        streetDirection.setText(calle);
        numberExteriorDirection.setText(number);
        cpDirection.setText(cp);
        colonyDirection.setText(colonia);
        cityDirection.setText(ciudad);
        stateDirection.setText(estado);
    }


}
