package lunainc.com.mx.fastdelivery.UI.Activity.Client;

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

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import lunainc.com.mx.fastdelivery.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.com.mx.fastdelivery.Adapter.FavoriteProductAdapter;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.FavoriteProduct;
import lunainc.com.mx.fastdelivery.Model.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity implements FavoriteProductAdapter.ItemClickListener{


    @BindView(R.id.recyclerView)
    ShimmerRecyclerView recyclerView;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String token = "";
    private APIService apiService;
    private SharedPreferences sharedPref;
    private Context context;

    private ArrayList<FavoriteProduct> favorites;
    private FavoriteProductAdapter favoriteProductAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ButterKnife.bind(this);



        configViews();
        initVars();

    }

    public void initVars() {
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(
                "credentials", Context.MODE_PRIVATE);

        apiService = ApiUtils.getAPIService();
        token = "Bearer " +  sharedPref.getString(("token"), "noLogged");





    }


    private void configViews(){
        toolbar.setTitle("Tus Favoritos");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    protected void onStart() {
        super.onStart();

        setData();


    }

    private void setData(){
        favorites = loadData();

        reloadData();
        recyclerView.showShimmerAdapter();

        recyclerView.postDelayed(() -> recyclerView.hideShimmerAdapter(),3000);


        refresh.setOnRefreshListener(() -> {
            reloadData();

            recyclerView.showShimmerAdapter();

            recyclerView.postDelayed(() -> {
                recyclerView.hideShimmerAdapter();
                refresh.setRefreshing(false);
            },3000);


        });

    }




    public ArrayList<FavoriteProduct> loadData(){
        ArrayList<FavoriteProduct> fav = new ArrayList<FavoriteProduct>();

        apiService.showFavoriteProducts("Accept", token).enqueue(new Callback<FavoriteProduct>() {
            @Override
            public void onResponse(@NotNull Call<FavoriteProduct> call, @NotNull Response<FavoriteProduct> response) {
                if (response.isSuccessful()){

                    int tam = response.body().getFavorites().size();

                    if (tam > 0){

                        for (int i = 0; i < tam; i++) {
                            FavoriteProduct favoriteProduct = response.body().getFavorites().get(i);
                            fav.add(favoriteProduct);
                        }


                    }

                }

            }

            @Override
            public void onFailure(@NotNull Call<FavoriteProduct> call, @NotNull Throwable t) {

            }
        });



        return fav;
    }


    @Override
    public void onItemClick(View view, int position) {
        Product product = favorites.get(position).getProduct();

        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra("product_id", product.getId());
        intent.putExtra("type_product", product.getType_product());
        startActivity(intent);



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        if (item.getItemId() == android.R.id.home){
                finish();
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }


    private void reloadData(){
        favorites = loadData();
        favoriteProductAdapter = new FavoriteProductAdapter(context, favorites);
        favoriteProductAdapter.notifyDataSetChanged();
        favoriteProductAdapter.setClickListener(FavoritesActivity.this);
        recyclerView.setAdapter(favoriteProductAdapter);
    }

}
