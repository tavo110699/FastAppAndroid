package lunainc.com.mx.fastdelivery.UI.Activity.Socio.Ingredients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class CreateIngredientsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nameIngredient)
    TextInputEditText nameIngredient;

    @BindView(R.id.priceIngrediente)
    TextInputEditText priceIngredient;

    @BindView(R.id.actionBtn)
    FloatingActionButton actionButton;

    private String token = "";

    private APIService apiService;
    private SharedPreferences sharedPref;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ingredients);
        ButterKnife.bind(this);
        configToolbar();
        initVars();



    }


    public void configToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.add_ingredients_title);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    private void initVars() {
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(
                "credentials", Context.MODE_PRIVATE);
        token = "Bearer " +sharedPref.getString(("token"), "noLogged");
        apiService = ApiUtils.getAPIService();
    }



    @Override
    protected void onStart() {
        super.onStart();

        events();
    }

    private void events() {

        actionButton.setOnClickListener(view -> {

            String name = nameIngredient.getText().toString();
            String price = priceIngredient.getText().toString();


            if (name.trim().length() > 0 && price.trim().length() > 0){
                actionButton.setEnabled(false);

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("name", name)
                        .addFormDataPart("price", price)
                        .build();

                apiService.createIngredient("Accept", token, requestBody).enqueue(new Callback<ResponseDefault>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {

                        if (response.isSuccessful()){


                            if (response.body().getStatus().equals("success")){
                                clearInputs();
                                Toasty.success(CreateIngredientsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT, true).show();

                            }else{
                                Toasty.warning(CreateIngredientsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT, true).show();

                            }


                        }else {
                            Toasty.warning(CreateIngredientsActivity.this, "Ocurrio un error " + response.code(), Toast.LENGTH_SHORT, true).show();

                        }


                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {
                        Toasty.warning(CreateIngredientsActivity.this, "Ocurrio un error: " + t.getMessage(), Toast.LENGTH_SHORT, true).show();

                    }
                });


            }else {
                Toasty.warning(CreateIngredientsActivity.this, R.string.complete_all_inputs, Toast.LENGTH_SHORT, true).show();

            }

            actionButton.setEnabled(true);








        });


    }



    public void clearInputs(){
        nameIngredient.setText(null);
        priceIngredient.setText(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case android.R.id.home:
                gotToBack();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        gotToBack();
    }

    public void gotToBack(){
        new Constants().goTo(this, IngredientsPartnerActivity.class);
    }

}
