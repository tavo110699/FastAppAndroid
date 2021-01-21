package lunainc.com.mx.fastdelivery.UI.Activity.Socio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import lunainc.com.mx.fastdelivery.R;import com.tiper.MaterialSpinner;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.Order;
import lunainc.com.mx.fastdelivery.Model.OrderProducts;
import lunainc.com.mx.fastdelivery.Model.ResponseDefault;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import lunainc.com.mx.fastdelivery.customfonts.MyTextView_Roboto_Regular;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailOrderActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nameClient)
    MyTextView_Roboto_Regular nameClient;

    @BindView(R.id.directionClient)
    MyTextView_Roboto_Regular directionClient;

    @BindView(R.id.priceTotal)
    MyTextView_Roboto_Regular priceTotal;

    @BindView(R.id.descriptionProduct)
    MyTextView_Roboto_Regular descriptionProduct;

    @BindView(R.id.speedDial)
    SpeedDialView speedDial;

    private Order order;
    private APIService apiService;
    private Context context;
    private String token = "";
    private ArrayList<String> orderStatusDefault;
    private ArrayAdapter<String> adapterOrderStatus;
    private String detailsGG = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        ButterKnife.bind(this);
        configViews();
        initVars();
        getIntentsFromActivity();

    }


    private void configViews(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    private void initVars(){
        context = this;
        apiService = ApiUtils.getAPIService();
        token = new Constants().getToken(context);
        orderStatusDefault = new ArrayList<>();

        orderStatusDefault.add("Enterado");
        orderStatusDefault.add("Enviado");
        orderStatusDefault.add("Entregado");

        adapterOrderStatus = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, orderStatusDefault);

    }

    private void getIntentsFromActivity(){
        order = (Order) getIntent().getSerializableExtra("order");
    }


    @Override
    protected void onStart() {
        super.onStart();
        setDataInView();
        events();
    }




    @SuppressLint("SetTextI18n")
    private void setDataInView(){

        nameClient.setText(order.getUser().getName());
        directionClient.setText(order.getDirection().getDirection_complete());
        priceTotal.setText(order.getTotal()+" MXN");

        int sizeProducts = order.getOrderProducts().size();
        detailsGG = "";
        for (int i = 0; i < sizeProducts; i++) {
            OrderProducts orderProducts = order.getOrderProducts().get(i);

            detailsGG = detailsGG + orderProducts.getProduct().getName()
            +" Cantidad: "+orderProducts.getQuantity()+" Precio: "+orderProducts.getPrice()+" Extra: "+orderProducts.getExtra()
            +"\n"+orderProducts.getDetails()+"\n\n";

        }


        descriptionProduct.setText(detailsGG);



    }


    private void events() {

        if (!order.getStatus().equals("Entregado")){
            speedDial.addActionItem(
                    new SpeedDialActionItem.Builder(R.id.status,R.drawable.ic_status)
                            .setFabBackgroundColor(getResources().getColor(R.color.blue_grey_light))
                            .setLabel(R.string.change_status)
                            .create());
        }


        speedDial.addActionItem(
                new SpeedDialActionItem.Builder(R.id.share,R.drawable.ic_share_new)
                        .setFabBackgroundColor(getResources().getColor(R.color.blue_grey_light))
                        .setLabel(R.string.send_info)
                        .create());

        speedDial.addActionItem(
            new SpeedDialActionItem.Builder(R.id.assign, R.drawable.ic_partner_motorcycle)
                    .setFabBackgroundColor(getResources().getColor(R.color.blue_grey_light))
                    .setLabel(R.string.assign_order)
                    .create());



        speedDial.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()){
                    case R.id.status:
                        showChangeStatusDialog();
                        speedDial.close();
                        return true;
                    case R.id.share:

                        String principal = "Orden para "+order.getUser().getName()+" "+order.getUser().getLast_name_p()+" "+order.getUser().getLast_name_m();
                        principal = principal + "\n\nTotal: "+order.getTotal()+" MXN";

                        principal = principal + detailsGG;

                        shareAction(principal, order.getPartner().getName());


                        speedDial.close();
                        break;
                    case R.id.assign:
                        speedDial.close();
                        break;
                    default:
                        speedDial.close();
                        break;


                }

                return false;
            }


        });

    }

    private void shareAction(String info, String partner){

        String direction = "http://maps.google.com/?q="+order.getDirection().getLatitude()+","+order.getDirection().getLongitude();

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Información de la orden: \n\n"+info+"\n\n"+direction);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, partner+" - Fast Delivery App");
        startActivity(Intent.createChooser(sharingIntent, "Compartir en: "));

    }


    @SuppressLint("SetTextI18n")
    private void showChangeStatusDialog(){

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_change_status, null);
        TextView orderStatus = (TextView) view.findViewById(R.id.orderStatus);
        MaterialSpinner spinner = (MaterialSpinner) view.findViewById(R.id.spinner);

        spinner.setAdapter(adapterOrderStatus);


        orderStatus.setText("Estado de la orden: "+order.getStatus());


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(view)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateStatusOrder(orderStatusDefault.get(spinner.getSelection()));
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();



    }


    private void updateStatusOrder(String status){

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_order", order.getId())
                .addFormDataPart("status", status)
                .build();


        apiService.changeStatusOrder("Accept", token, body).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {

                if (response.isSuccessful()){

                    if (response.body().getStatus().equals("success")){
                        order.setStatus(status);
                        Toasty.success(context, "Se cambio el estado de la orden").show();

                        if (response.body().getMessage().equals("Entregado")){
                            gotToBack();
                        }


                    }else{
                        Toasty.error(context, response.body().getMessage()).show();
                    }

                }

            }

            @Override
            public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == android.R.id.home) {
            gotToBack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        gotToBack();
    }



    public void gotToBack(){
        new Constants().goTo(this, HomePartnerActivity.class);
    }


}
