
package lunainc.com.mx.fastdelivery.UI.Fragment.Client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import lunainc.com.mx.fastdelivery.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import lunainc.com.mx.fastdelivery.Adapter.Partner.OrderAdapter;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.Order;
import lunainc.com.mx.fastdelivery.UI.Activity.Client.DetailOrderClientActivity;
import lunainc.com.mx.fastdelivery.UI.Activity.Socio.DetailOrderActivity;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialOrdersFragment extends Fragment implements OrderAdapter.ItemClickListener {

    private View view;

    @BindView(R.id.recyclerView)
    ShimmerRecyclerView recyclerView;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    private ArrayList<Order> orders;
    private OrderAdapter orderAdapter;
    private Context context;
    private String token = "";
    private APIService apiService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);
        configViews();
        initVars();
        return view;
    }

    private void configViews(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initVars(){

        context = getActivity();

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

    }

    public ArrayList<Order> loadData(){
        ArrayList<Order> orderArrayList = new ArrayList<Order>();



        apiService.getOrders("Accept", token).enqueue(new Callback<Order>() {
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
        Intent intent = new Intent(getActivity(), DetailOrderClientActivity.class);
        intent.putExtra("order", order);
        startActivity(intent);

    }
}
