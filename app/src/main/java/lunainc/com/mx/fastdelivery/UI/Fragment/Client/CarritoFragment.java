package lunainc.com.mx.fastdelivery.UI.Fragment.Client;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import lunainc.com.mx.fastdelivery.R;import com.tiper.MaterialSpinner;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import lunainc.com.mx.fastdelivery.Adapter.CestaAdapter;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.Cesta;
import lunainc.com.mx.fastdelivery.Model.Direction;
import lunainc.com.mx.fastdelivery.Model.Order;
import lunainc.com.mx.fastdelivery.Model.Product;
import lunainc.com.mx.fastdelivery.Model.ResponseDefault;
import lunainc.com.mx.fastdelivery.UI.Activity.Client.HomeActivity;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import lunainc.com.mx.fastdelivery.Utils.DBHelper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarritoFragment extends Fragment implements CestaAdapter.ItemLongClickListener {

    private View view;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;


    @BindView(R.id.containerEmptyShoppingCart)
    LinearLayout containerEmptyShoppingCart;


    @BindView(R.id.recyclerView)
    ShimmerRecyclerView recyclerView;

    @BindView(R.id.buttonAdd)
    ExtendedFloatingActionButton buttonAdd;

    private Context context;
    private DBHelper db;
    private ArrayList<Cesta> cestas;
    private CestaAdapter cestaAdapter;
    private APIService apiService;
    private String token = "";

    private ProgressDialog progressDialog;
    private ProgressDialog updatePricesDialog;

    private ArrayList<Direction> directions;
    private ArrayList<String> directionsString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_carrito, container, false);
        ButterKnife.bind(this, view);
        initVars();
        return view;
    }


    public void initVars() {
        context = Objects.requireNonNull(getActivity()).getApplicationContext();
        db = new DBHelper(context);
        apiService = ApiUtils.getAPIService();
        token = new Constants().getToken(context);


        progressDialog = new ProgressDialog(getActivity());
        updatePricesDialog = new ProgressDialog(getActivity());
        directionsString = new ArrayList<>();

        updatePricesDialog.setTitle("Actualizando datos");
        updatePricesDialog.setMessage("Espere un momento por favor");
        updatePricesDialog.setCancelable(false);
        updatePricesDialog.show();
        updatePricesShoppingCart();
        configViews();

        if (db.getProducts().size() <= 0){
            buttonAdd.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            containerEmptyShoppingCart.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setVisibility(View.VISIBLE);
            containerEmptyShoppingCart.setVisibility(View.GONE);

        }


    }

    private void configViews(){
        toolbar.setTitle("Cesta de productos");
        ((HomeActivity) Objects.requireNonNull(getActivity())).loadBadgeInfo();

        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(context);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.findFirstVisibleItemPosition();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        directions = getDirectionsFromServer();
    }


    @Override
    public void onStart() {
        super.onStart();
        events();
    }

    private void loadData(){
        reloadData();

        recyclerView.showShimmerAdapter();

        recyclerView.postDelayed(() -> recyclerView.hideShimmerAdapter(), 3000);


        refresh.setOnRefreshListener(() -> {
            reloadData();

            recyclerView.showShimmerAdapter();

            recyclerView.postDelayed(() -> {
                recyclerView.hideShimmerAdapter();
                refresh.setRefreshing(false);
                ((HomeActivity) Objects.requireNonNull(getActivity())).loadBadgeInfo();
            }, 3000);

        });


        if ((HomeActivity)getActivity() != null ) {

            ((HomeActivity) Objects.requireNonNull(getActivity())).loadBadgeInfo();
        }
    }


    @Override
    public void onItemLongClick(View view, int position) {
        deleteProduct(position);
    }


    private void confirmOrder(){
        Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_confirm_shoppingcart);

        Button cancelBtn = (Button) dialog.findViewById(R.id.btnCancel);
        Button addBtn = (Button) dialog.findViewById(R.id.btnAdd);
        MaterialSpinner spinner = (MaterialSpinner) dialog.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, directionsString);
        spinner.setAdapter(adapter);

        setDataInDialog(dialog);

        cancelBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });


        addBtn.setOnClickListener(v -> {

            int position = spinner.getSelection();

            createOrder(position);

            dialog.dismiss();
        });

        dialog.show();

    }



    @SuppressLint("SetTextI18n")
    private void setDataInDialog(Dialog dialog) {
        progressDialog.setTitle("Cargando información");
        progressDialog.setMessage("Espere un momento, estamos preparando tu orden");
        progressDialog.setCancelable(false);
        progressDialog.show();

        TextView numProductsTextView = (TextView) dialog.findViewById(R.id.num);
        numProductsTextView.setText("Número de ordenes: "+db.getNumOrder());
        TextView priceDialg = (TextView) dialog.findViewById(R.id.price);


        priceDialg.setText(getTotalToPayment());
    }


    private void deleteProduct(int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setMessage("¿Realmente desea quitar este producto de su cesta?")
                .setTitle("Eliminar producto de la cesta")
                .setPositiveButton("Aceptar", (dialog, id) -> {

                    db.deleteProduct(String.valueOf(cestas.get(position).getId()));
                    reloadData();

                })
                .setNegativeButton("Cancelar", (dialog, id) -> dialog.dismiss());
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();

    }

    private void events(){
        buttonAdd.setOnClickListener(v-> {


            confirmOrder();

        });


    }

    private ArrayList<Direction> getDirectionsFromServer(){
        ArrayList<Direction> directionArrayList = new ArrayList<>();

        apiService.getDirections("Accept", token).enqueue(new Callback<Direction>() {
            @Override
            public void onResponse(@NotNull Call<Direction> call, @NotNull Response<Direction> response) {

                if (response.isSuccessful()){

                    if (response.body().getStatus().equals("success")){

                        int tam = response.body().getDirections().size();

                        if (tam > 0){

                            for (int i = 0; i < tam; i++) {
                                Direction direction = response.body().getDirections().get(i);
                                directionArrayList.add(direction);
                                directionsString.add(direction.getDirection_complete());

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

    private String getTotalToPayment(){
        String result = "";
        int size =db.getPartnerID().size();
        for (int i = 0; i < size; i++) {


            String partner_id = db.getPartnerID().get(i);
            result = result + "El costo de la orden a "+db.getPartnerName().get(i)+" es de: "
                    +String.valueOf(getPriceProduct(db.getProductsByPartnerID(partner_id)))+ "\n";


        }


        progressDialog.dismiss();




        return result;
    }



    private void createOrder(int positionDirection){
        int size =db.getPartnerID().size();
        progressDialog.setTitle("Creando orden");
        progressDialog.setMessage("Espere un momento, estamos creando su orden");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String id_direction = directions.get(positionDirection).getId();


        for (int i = 0; i < size; i++) {

            String partner_id = db.getPartnerID().get(i);
            requestCreateOrder(partner_id, id_direction);


        }

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressDialog.dismiss();
                Toasty.success(context, "Haz realizado tus pedidos").show();
                db.deleteAllProducts();


            }
        }, 300);


    }


    private void requestCreateOrder(String partner_id, String id_direction){

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_partner", partner_id)
                .addFormDataPart("id_direction", id_direction)
                .addFormDataPart("total", String.valueOf(getPriceProduct(db.getProductsByPartnerID(partner_id))))
                .build();

        apiService.createOrders("Accept", token, body).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NotNull Call<Order> call, @NotNull Response<Order> response) {

                if (response.isSuccessful()){

                    if (Objects.requireNonNull(response.body()).getStatus_res().equals("success")){
                        addProductsToOrder(db.getProductsByPartnerID(partner_id), response.body().getId());
                        sendNotificationOrder(partner_id, response.body().getId());
                    }

                }

            }

            @Override
            public void onFailure(@NotNull Call<Order> call, @NotNull Throwable t) {

            }
        });





    }

    private void sendNotificationOrder(String partner_id, String order_id){
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_partner", partner_id)
                .addFormDataPart("id_order", order_id)
                .build();


        apiService.sendNotificationToPartner("Accept", token, body).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus().equals("success")){
                        Toasty.success(context, response.body().getMessage()).show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private float getPriceProduct(ArrayList<Cesta> cesta){
        float sum = 0;
        int size = cesta.size();

        for (int i = 0; i < size; i++) {
            sum = sum + (  (Float.parseFloat(cesta.get(i).getStatic_price_product()) * Integer.parseInt(cesta.get(i).getCantidad())) + Float.parseFloat(cesta.get(i).getExtra()) );

        }

        return sum;
    }

    private void addProductsToOrder(ArrayList<Cesta> cestas, String order_id){

        int sizeProducts = cestas.size();
        for (int i = 0; i < sizeProducts; i++) {


            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("id_order", order_id)
                    .addFormDataPart("id_product", cestas.get(i).getId_product())
                    .addFormDataPart("extra", cestas.get(i).getExtra())
                    .addFormDataPart("quantity", cestas.get(i).getCantidad())
                    .addFormDataPart("details", cestas.get(i).getDescripcion())
                    .build();


            apiService.addProductsToOrders("Accept", token, body).enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {

                    if (response.isSuccessful()){

                        if (Objects.requireNonNull(response.body()).getStatus().equals("success")){
                            Log.e("SuccessAddProdToOrder", response.body().getMessage());
                        }

                    }else{
                        Log.e("ErrorAddProdToOrder", response.toString());
                    }


                }

                @Override
                public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {
                    Log.e("ErrorAddProdToOrder", Objects.requireNonNull(t.getMessage()));
                }
            });



        }



    }


    private void reloadData(){
        cestas = db.getProducts();
        cestaAdapter = new CestaAdapter(context,cestas);
        cestaAdapter.notifyDataSetChanged();
        cestaAdapter.setLongClickListener(this);
        recyclerView.setAdapter(cestaAdapter);
        if ((HomeActivity)getActivity() != null) {
            ((HomeActivity) Objects.requireNonNull(getActivity())).loadBadgeInfo();
        }
    }


    private void updatePricesShoppingCart(){
        cestas = db.getProducts();

        int size = cestas.size();

        for (int i = 0; i < size; i++) {
            Cesta cesta = cestas.get(i);
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("product_id", cesta.getId_product())
                    .addFormDataPart("type_product", cesta.getType_product())
                    .build();

            apiService.getProductClient("Accept", token, body).enqueue(new Callback<Product>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NotNull Call<Product> call, @NotNull Response<Product> response) {


                    if (response.isSuccessful()){


                        if (Objects.requireNonNull(response.body()).getStatus().equals("success")){
                            Product product = response.body();
                            db.updateProduct("static_price_product", product.getPrice());
                            db.updateProduct("product_name", product.getName());
                            db.updateProduct("product_image", product.getImage());

                        }

                    }


                }

                @Override
                public void onFailure(@NotNull Call<Product> call, @NotNull Throwable t) {

                }
            });


        }

        updatePricesDialog.dismiss();

        new android.os.Handler().postDelayed(this::loadData,1000);



    }


}
