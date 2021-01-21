package lunainc.com.mx.fastdelivery.UI.Activity.Client.Directions;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import lunainc.com.mx.fastdelivery.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import lunainc.com.mx.fastdelivery.Adapter.DirectionAdapter;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.Direction;
import lunainc.com.mx.fastdelivery.Model.ResponseDefault;
import lunainc.com.mx.fastdelivery.UI.Activity.Client.PermissionsActivity;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DirectionsActivity extends AppCompatActivity implements DirectionAdapter.ItemClickListener, DirectionAdapter.ItemLongClickListener {

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
    private Context context;
    private ArrayList<Direction> directions;
    private DirectionAdapter directionAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        ButterKnife.bind(this);
        configViews();
        initVars();

    }
    private void configViews(){
        setSupportActionBar(toolbar);
        toolbar.setTitle("Mis direcciones - Fast App");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initVars() {
        context = this;

        token = new Constants().getToken(context);

        apiService = ApiUtils.getAPIService();


    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermission();
        loadData();
        setDataInView();
        events();
        if (ActivityCompat.checkSelfPermission(DirectionsActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(DirectionsActivity.this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }



    }

    private void events() {
        actionButton.setOnClickListener(v -> new Constants().goTo(this, PermissionsActivity.class));
    }


    private void loadData(){
        directions = getDirectionsFromServer();
        directionAdapter = new DirectionAdapter(context, directions);
        directionAdapter.notifyDataSetChanged();
        directionAdapter.setClickListener(this);
        directionAdapter.setLongClickListener(this);
        recyclerView.setAdapter(directionAdapter);
    }

    private void setDataInView(){
        recyclerView.showShimmerAdapter();

        recyclerView.postDelayed(() -> recyclerView.hideShimmerAdapter(), 3000);
        refresh.setOnRefreshListener(() -> {

            loadData();
            recyclerView.showShimmerAdapter();

            recyclerView.postDelayed(() -> recyclerView.hideShimmerAdapter(), 3000);
            refresh.setRefreshing(false);

        });

    }


    private ArrayList<Direction> getDirectionsFromServer(){
        ArrayList<Direction> directionArrayList = new ArrayList<>();

        apiService.getDirections("Accept", token).enqueue(new Callback<Direction>() {
            @Override
            public void onResponse(@NotNull Call<Direction> call, @NotNull Response<Direction> response) {

                if (response.isSuccessful()){

                    assert response.body() != null;
                    if (response.body().getStatus().equals("success")){

                        int tam = response.body().getDirections().size();

                        if (tam > 0){

                            for (int q = 0; q < tam; q++) {

                                Direction direction = response.body().getDirections().get(q);
                                directionArrayList.add(direction);

                            }

                        }

                    }


                }

            }

            @Override
            public void onFailure(@NotNull Call<Direction> call, @NotNull Throwable t) {

            }
        });






        return directionArrayList;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        return true;
        }
        return false;
    }



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }


    @Override
    public void onItemClick(View view, int position) {
        deleteDirection(directions.get(position).getId());
    }

    @Override
    public void onItemLongClick(View view, int position) {
        deleteDirection(directions.get(position).getId());

    }

    private void deleteDirection(String id_direction){

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(this));
        builder.setTitle("Eliminar la dirección, está acción no se puede deshacer")
                .setMessage("¿Realmente desea eliminar esta dirección?")
                .setPositiveButton("Aceptar", (dialog, id) -> {

                    RequestBody body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("id_direction", id_direction)
                            .build();

                    apiService.deleteDirection("Accept", token, body).enqueue(new Callback<ResponseDefault>() {
                        @Override
                        public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {

                            if (response.isSuccessful()){

                                if (response.body().getStatus().equals("success")){
                                    Toasty.success(context, response.body().getMessage()).show();
                                    loadData();
                                }else{
                                    Toasty.error(context, response.body().getMessage()).show();

                                }

                            }

                        }

                        @Override
                        public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {

                        }
                    });

                })
                .setNegativeButton("Cancelar", (dialog, id) -> dialog.dismiss());
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();

    }



    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, 1);
    }

}
