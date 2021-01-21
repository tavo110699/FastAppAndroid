package lunainc.com.mx.fastdelivery.UI.Activity.Socio.Ingredients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import lunainc.com.mx.fastdelivery.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import lunainc.com.mx.fastdelivery.Adapter.Partner.IngredientAdapter;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.Ingredient;
import lunainc.com.mx.fastdelivery.UI.Activity.Socio.HomePartnerActivity;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientsPartnerActivity extends AppCompatActivity implements IngredientAdapter.ItemClickListener, IngredientAdapter.ItemLongClickListener {


    @BindView(R.id.recyclerView)
    ShimmerRecyclerView recyclerView;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    @BindView(R.id.actionButton)
    FloatingActionButton actionButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String token = "";

    private APIService apiService;
    private SharedPreferences sharedPref;
    private Context context;

    private ArrayList<Ingredient> ingredients;
    private IngredientAdapter ingredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_partner);
        ButterKnife.bind(this);

        configViews();
        initVars();

    }

    private void configViews(){
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.my_ingredients_title);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void initVars() {
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(
                "credentials", Context.MODE_PRIVATE);
        token = "Bearer " +sharedPref.getString(("token"), "noLogged");

        apiService = ApiUtils.getAPIService();



        ingredients = loadData();
        reloadData();
        recyclerView.showShimmerAdapter();


        recyclerView.postDelayed(() -> recyclerView.hideShimmerAdapter(),3000);


        refresh.setOnRefreshListener(() -> {
            reloadData();

            recyclerView.postDelayed(() -> {
                recyclerView.hideShimmerAdapter();
                refresh.setRefreshing(false);

            }, 3000);




        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        events();

    }


    public ArrayList<Ingredient> loadData(){

         ArrayList<Ingredient> ingre = new ArrayList<Ingredient>();

        apiService.getIngredients("Accept", token).enqueue(new Callback<Ingredient>() {
            @Override
            public void onResponse(@NotNull Call<Ingredient> call, @NotNull Response<Ingredient> response) {

                if (response.isSuccessful()){

                    if (response.body().getStatus().equals("success")){

                        int tam = response.body().getIngredients().size();

                        if ( tam > 0){

                            for (int i = 0; i < tam; i++) {
                                Ingredient ingredient = (Ingredient) response.body().getIngredients().get(i);
                                ingre.add(ingredient);
                            }
                        }
                    }else {
                        Toasty.warning(IngredientsPartnerActivity.this, "Ocurrio un error " + response.code(), Toast.LENGTH_SHORT, true).show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Ingredient> call, @NotNull Throwable t) {
                Toasty.warning(IngredientsPartnerActivity.this, "Ocurrio un error: " + t.getMessage(), Toast.LENGTH_SHORT, true).show();

            }
        });

        return ingre;
    }



    private void events() {

        actionButton.setOnClickListener(view -> goTo(CreateIngredientsActivity.class));

    }



    public void goTo(Class clase){
        Intent intent = new Intent(IngredientsPartnerActivity.this, clase);
        startActivity(intent);
        finish();

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemLongClick(View view, int position) {

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
        new Constants().goTo(this, HomePartnerActivity.class);
    }


    private void reloadData(){
        ingredients = loadData();
        ingredientAdapter = new IngredientAdapter(IngredientsPartnerActivity.this, ingredients);
        ingredientAdapter.setClickListener(this);
        ingredientAdapter.setLongClickListener(this);
        ingredientAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(ingredientAdapter);
    }

}
