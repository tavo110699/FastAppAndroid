package lunainc.com.mx.fastdelivery.UI.Activity.Socio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import lunainc.com.mx.fastdelivery.R;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import lunainc.com.mx.fastdelivery.Adapter.Partner.OrderAdapter;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.MessageEventPartnerOrder;
import lunainc.com.mx.fastdelivery.Model.Order;
import lunainc.com.mx.fastdelivery.Model.Partner;
import lunainc.com.mx.fastdelivery.UI.Activity.Socio.Ingredients.IngredientsPartnerActivity;
import lunainc.com.mx.fastdelivery.UI.Activity.Socio.Product.ProductsPartnerActivity;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePartnerActivity extends AppCompatActivity implements OrderAdapter.ItemClickListener {


    @BindView(R.id.recyclerView)
    ShimmerRecyclerView recyclerView;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    @BindView(R.id.speedDial)
    SpeedDialView speedDial;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ArrayList<Order> orders;
    private OrderAdapter orderAdapter;

    private String token = "";
    private APIService apiService;
    private SharedPreferences sharedPref;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_partner);

        ButterKnife.bind(this);
        configViews();
        initVars();

    }

    private void configViews(){
        setSupportActionBar(toolbar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void initVars(){
        context = this;

        apiService = ApiUtils.getAPIService();
        token = new Constants().getToken(context);




        reloadData();
        recyclerView.showShimmerAdapter();


        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.hideShimmerAdapter();
            }
        }, 3000);


        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                reloadData();
                recyclerView.showShimmerAdapter();
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.hideShimmerAdapter();
                        refresh.setRefreshing(false);
                    }
                },3000);

            }
        });


        apiService.getPartner("Accept", token).enqueue(new Callback<Partner>() {
            @Override
            public void onResponse(@NotNull Call<Partner> call, @NotNull Response<Partner> response) {

                if (response.isSuccessful()){


                    toolbar.setTitle("Fast App - "+response.body().getName());

                }


            }

            @Override
            public void onFailure(@NotNull Call<Partner> call, @NotNull Throwable t) {
                Toasty.warning(context, "Ocurrio un error: " + t.getMessage(), Toast.LENGTH_SHORT, true).show();

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();




        new Constants().updateDeviceToken(apiService, token);

        events();

        EventBus.getDefault().register(this);

    }

    private void events() {

        speedDial.addActionItem(
                new SpeedDialActionItem.Builder(R.id.products,R.drawable.ic_products)
                        .setFabBackgroundColor(getResources().getColor(R.color.blue_grey_light))
                        .setLabel(R.string.my_products)
                        .create());

        speedDial.addActionItem(
                new SpeedDialActionItem.Builder(R.id.ingredients,R.drawable.ic_ingredient)
                        .setFabBackgroundColor(getResources().getColor(R.color.blue_grey_light))
                        .setLabel(R.string.my_ingredients)
                        .create());

      speedDial.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
          @Override
          public boolean onActionSelected(SpeedDialActionItem actionItem) {
              switch (actionItem.getId()){
                  case R.id.products:
                      speedDial.close();
                      goTo(ProductsPartnerActivity.class);
                      return true;
                  case R.id.ingredients:
                      speedDial.close();
                        goTo(IngredientsPartnerActivity.class);
                      break;
                  default:
                      speedDial.close();
                      break;


              }

              return false;
          }


      });

    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();

    }

    public ArrayList<Order> loadData(){
         ArrayList<Order> orderArrayList = new ArrayList<Order>();



        apiService.getOrdersPartner("Accept", token).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NotNull Call<Order> call, @NotNull Response<Order> response) {

                if (response.isSuccessful()){

                    if (response.body().getStatus_res().equals("success")){

                        int tam = response.body().getOrders().size();

                        if ( tam > 0) {

                            for (int i = 0; i < tam; i++) {
                                Order order = response.body().getOrders().get(i);
                                orders = response.body().getOrders();
                                orderArrayList.add(order);
                            }

                        }

                    }


                }else {
                    Toasty.warning(context, "Ocurrio un error " + response.code(), Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFailure(@NotNull Call<Order> call, @NotNull Throwable t) {
                Toasty.warning(context, "Ocurrio un error: " + t.getMessage(), Toast.LENGTH_SHORT, true).show();

            }
        });

        return orderArrayList;
    }





    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEventPartnerOrder event) {
        if (event.getMessage().equals("partner")){

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("id_order", event.getId_order())
                    .build();

            apiService.getOrderPartner("Accept", token, body).enqueue(new Callback<Order>() {
                @Override
                public void onResponse(@NotNull Call<Order> call, @NotNull Response<Order> response) {

                    if (response.isSuccessful()){

                        if (response.body().getStatus_res().equals("success")){

                            int position = orders.size();
                            Order order = response.body();
                            orders.add(position,order);

                            orderAdapter = new OrderAdapter(context, orders);
                            orderAdapter.notifyItemInserted(position);
                            orderAdapter.setClickListener(HomePartnerActivity.this);
                            orderAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(orderAdapter);


                        }

                    }

                }

                @Override
                public void onFailure(@NotNull Call<Order> call, @NotNull Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });



        }



    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_partner, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
       if (item.getItemId() == R.id.action_settings_partner) {
           new Constants().goTo(this, SettingsPartnerActivity.class);
       }

        return super.onOptionsItemSelected(item);
    }




    public void goTo(Class clase){
        Intent intent = new Intent(HomePartnerActivity.this, clase);
        startActivity(intent);
        finish();

    }



    private void reloadData(){
        orders = loadData();
        orderAdapter = new OrderAdapter(context, orders);
        orderAdapter.setClickListener(this);
        orderAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(orderAdapter);
    }


    @Override
    public void onItemClick(View view, int position) {
        Order order = orders.get(position);
        Intent intent = new Intent(this, DetailOrderActivity.class);
        intent.putExtra("order", order);
        startActivity(intent);
        finish();

    }
}
