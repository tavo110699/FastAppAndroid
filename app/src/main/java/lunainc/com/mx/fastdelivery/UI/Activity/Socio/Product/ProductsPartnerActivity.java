package lunainc.com.mx.fastdelivery.UI.Activity.Socio.Product;

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
import lunainc.com.mx.fastdelivery.Adapter.Partner.ProductAdapter;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.Product;
import lunainc.com.mx.fastdelivery.UI.Activity.Socio.HomePartnerActivity;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsPartnerActivity extends AppCompatActivity implements ProductAdapter.ItemClickListener, ProductAdapter.ItemLongClickListener {

    @BindView(R.id.recyclerView)
    ShimmerRecyclerView recyclerView;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    @BindView(R.id.actionButton)
    FloatingActionButton actionButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String token = "";

    private ArrayList<Product> products;
    private ProductAdapter productAdapter;

    private APIService apiService;
    private SharedPreferences sharedPref;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_partner);
        ButterKnife.bind(this);

        configViews();
        initVars();

    }

    private void configViews(){
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.my_products_title);
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

        products = loadData();


        reloadData();
        recyclerView.showShimmerAdapter();


        recyclerView.postDelayed(() -> recyclerView.hideShimmerAdapter(), 3000);


        refresh.setOnRefreshListener(() -> {
            reloadData();

            recyclerView.showShimmerAdapter();

            recyclerView.postDelayed(() -> {
                recyclerView.hideShimmerAdapter();
                refresh.setRefreshing(false);
            },3000);


        });




    }


    @Override
    protected void onStart() {
        super.onStart();

        events();
    }


    public ArrayList<Product> loadData(){
        ArrayList<Product> productArrayList = new ArrayList<Product>();

        apiService.getProducts("Accept", token).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NotNull Call<Product> call, @NotNull Response<Product> response) {
                if (response.isSuccessful()){

                    if (response.body().getStatus().equals("success")){

                        int tam = response.body().getProducts().size();

                        if ( tam > 0){

                            for (int i = 0; i < tam; i++) {
                                Product product = response.body().getProducts().get(i);
                                productArrayList.add(product);
                            }


                        }


                    }else {
                        Toasty.warning(ProductsPartnerActivity.this, "Ocurrio un error " + response.code(), Toast.LENGTH_SHORT, true).show();

                    }

                }
            }

            @Override
            public void onFailure(@NotNull Call<Product> call, @NotNull Throwable t) {
                Toasty.warning(ProductsPartnerActivity.this, "Ocurrio un error: " + t.getMessage(), Toast.LENGTH_SHORT, true).show();

            }
        });


        return productArrayList;
    }


    private void events() {
        actionButton.setOnClickListener(view -> goTo(CreateProductActivity.class));

    }
    public void goTo(Class clase){
        Intent intent = new Intent(ProductsPartnerActivity.this, clase);
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
        products = loadData();

        productAdapter = new ProductAdapter(context, products);
        productAdapter.setLongClickListener(this);
        productAdapter.setClickListener(this);
        productAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(productAdapter);
    }

}
