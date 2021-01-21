package lunainc.com.mx.fastdelivery.UI.Fragment.Client;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import lunainc.com.mx.fastdelivery.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.com.mx.fastdelivery.Adapter.NotificationAdapter;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.Notification;
import lunainc.com.mx.fastdelivery.Model.ResponseDefault;
import lunainc.com.mx.fastdelivery.UI.Activity.Client.HomeActivity;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment  {



    @BindView(R.id.recyclerView)
    ShimmerRecyclerView recyclerView;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    @BindView(R.id.toolbar)
    Toolbar toolbar;




    private View view;


    private String token = "";
    private APIService apiService;
    private Context context;

    private NotificationAdapter notificationAdapter;
    private ArrayList<Notification> notifications;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.favorite_fragment, container, false);
        ButterKnife.bind(this, view);
        configViews();


        initVars();

        return view;
    }

    private void configViews(){
        toolbar.setTitle("Notificaciones");
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    public void initVars() {
        context = getActivity();


        apiService = ApiUtils.getAPIService();
        token = new Constants().getToken(context);



       reloadData();
        recyclerView.postDelayed(() -> recyclerView.hideShimmerAdapter(),3000);


        refresh.setOnRefreshListener(() -> {
           reloadData();
            recyclerView.postDelayed(() -> {
                recyclerView.hideShimmerAdapter();
                refresh.setRefreshing(false);
            },3000);


        });

        apiService.readAllNotifications("Accept", token).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {
                if (response.isSuccessful()){
                    Log.e("Success", "notificaciones leidas");
                    if ((HomeActivity)getActivity() != null){
                        ((HomeActivity) Objects.requireNonNull(getActivity())).getNotifications();
                    }

                }else {
                    Log.e("Error", "error al marcarlas como leidas");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {

            }
        });

    }


    public ArrayList<Notification> loadData(){
        ArrayList<Notification> noti = new ArrayList<Notification>();

        apiService.getNotifications("Accept", token).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(@NotNull Call<Notification> call, @NotNull Response<Notification> response) {
                if (response.isSuccessful()){

                    int tam = response.body().getNotifications().size();

                    if (tam > 0){

                        for (int i = 0; i < tam; i++) {
                            Notification notification = response.body().getNotifications().get(i);
                            noti.add(notification);
                        }


                    }

                }

            }

            @Override
            public void onFailure(@NotNull Call<Notification> call, @NotNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        return noti;
    }



    private void reloadData(){
        notifications = loadData();
        notificationAdapter = new NotificationAdapter(context, notifications);
        notificationAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(notificationAdapter);
        recyclerView.showShimmerAdapter();


    }

}
