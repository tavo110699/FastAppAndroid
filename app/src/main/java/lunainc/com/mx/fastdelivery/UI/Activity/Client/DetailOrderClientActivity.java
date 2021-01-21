package lunainc.com.mx.fastdelivery.UI.Activity.Client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.view.MenuItem;

import lunainc.com.mx.fastdelivery.R;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import lunainc.com.mx.fastdelivery.Model.Order;
import lunainc.com.mx.fastdelivery.Model.OrderProducts;

import lunainc.com.mx.fastdelivery.Utils.DateTime.DateTimeUtils;
import lunainc.com.mx.fastdelivery.customfonts.MyTextView_Roboto_Regular;

public class DetailOrderClientActivity extends AppCompatActivity {

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


    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order_client);
        ButterKnife.bind(this);
        configViews();
        getIntentsFromActivity();

    }


    private void configViews(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    private void getIntentsFromActivity(){
        order = (Order) getIntent().getSerializableExtra("order");
    }


    @Override
    protected void onStart() {
        super.onStart();
        setDataInView();

    }




    @SuppressLint("SetTextI18n")
    private void setDataInView(){

        if (order !=null) {
            if (order.getUser() != null) {
                nameClient.setText(order.getUser().getName() + " " + order.getUser().getLast_name_p() + " " + order.getUser().getLast_name_m());

            }

            directionClient.setText(order.getDirection().getDirection_complete());

            DateTimeUtils.setTimeZone("America/Mexico_City");
            Date date = DateTimeUtils.formatDate(order.getCreated_at());
            String timeAgo = DateTimeUtils.getTimeAgo(this, date);
            priceTotal.setText("Total: " + order.getTotal() + " MXN, orden realizada " + timeAgo);
            int sizeProducts = order.getOrderProducts().size();
            String detailsGG = "";
            for (int i = 0; i < sizeProducts; i++) {
                OrderProducts orderProducts = order.getOrderProducts().get(i);

                detailsGG = detailsGG + orderProducts.getProduct().getName()
                        + " Cantidad: " + orderProducts.getQuantity() + " Precio: " + orderProducts.getPrice() + " Extra: " + orderProducts.getExtra()
                        + "\n" + orderProducts.getDetails() + "\n\n";

            }

            descriptionProduct.setText(detailsGG);
        }


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
       finish();
    }


}
