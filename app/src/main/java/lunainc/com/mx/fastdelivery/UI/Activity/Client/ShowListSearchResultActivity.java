package lunainc.com.mx.fastdelivery.UI.Activity.Client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import lunainc.com.mx.fastdelivery.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import lunainc.com.mx.fastdelivery.Adapter.ProductClientAdapter;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.Product;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowListSearchResultActivity extends AppCompatActivity implements ProductClientAdapter.ItemClickListener, ProductClientAdapter.ItemLongClickListener {


    @BindView(R.id.recyclerView)
    ShimmerRecyclerView recyclerView;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String token = "";
    private String search = "";

    private ArrayList<Product> products;
    private ProductClientAdapter productClientAdapter;

    private APIService apiService;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_search_result);
        ButterKnife.bind(this);
        configView();
        initVars();
    }

    public void configView(){
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.my_products_title);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initVars() {
        context = getApplicationContext();

        token = new Constants().getToken(context);

        apiService = ApiUtils.getAPIService();
        search = getIntent().getStringExtra("search");
        toolbar.setTitle(search);


        products = loadData();

        reloadData();
        recyclerView.showShimmerAdapter();


        recyclerView.postDelayed(() -> recyclerView.hideShimmerAdapter(), 3000);


        refresh.setOnRefreshListener(() -> {

            reloadData();

            recyclerView.showShimmerAdapter();

            recyclerView.postDelayed(() -> {
                refresh.setRefreshing(false);
                recyclerView.hideShimmerAdapter();
            }, 3000);


        });



    }


    public ArrayList<Product> loadData(){
        ArrayList<Product> prod = new ArrayList<Product>();


        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("search",search)
                .build();

        apiService.searchProduct("Accept", token, body).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NotNull Call<Product> call, @NotNull Response<Product> response) {

                if (response.isSuccessful()){


                    int tam = response.body().getProducts().size();

                    if ( tam > 0) {

                        for (int i = 0; i < tam; i++) {
                            Product product = response.body().getProducts().get(i);
                            prod.add(product);

                        }

                    }



                }

            }

            @Override
            public void onFailure(@NotNull Call<Product> call, @NotNull Throwable t) {
                Toasty.warning(ShowListSearchResultActivity.this, "Ocurrio un error: " + t.getMessage(), Toast.LENGTH_SHORT, true).show();

            }
        });



        return prod;
    }

    @Override
    public void onItemClick(View view, int position) {
        Product product = products.get(position);
        Intent intent = new Intent(ShowListSearchResultActivity.this, ProductDetailActivity.class);
        intent.putExtra("product_id", product.getId());
        intent.putExtra("type_product", product.getType_product());
        startActivity(intent);
        //finish();

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
       finish();
    }


    private void reloadData(){
        products = loadData();

        productClientAdapter = new ProductClientAdapter(context, products);
        productClientAdapter.setClickListener(this);
        productClientAdapter.setLongClickListener(this);
        productClientAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(productClientAdapter);
    }

}
