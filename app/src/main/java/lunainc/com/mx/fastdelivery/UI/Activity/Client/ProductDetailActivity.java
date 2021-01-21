package lunainc.com.mx.fastdelivery.UI.Activity.Client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import lunainc.com.mx.fastdelivery.R;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.Ingredient;
import lunainc.com.mx.fastdelivery.Model.Product;
import lunainc.com.mx.fastdelivery.Model.ResponseDefault;
import lunainc.com.mx.fastdelivery.UI.Fragment.Client.AddIngredientsFragment;
import lunainc.com.mx.fastdelivery.UI.Fragment.Client.ModifyIngredientsFragment;
import lunainc.com.mx.fastdelivery.Utils.DBHelper;
import lunainc.com.mx.fastdelivery.customfonts.MyTextView_Roboto_Regular;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.imageProduct)
    ImageView imageProduct;

    @BindView(R.id.nameProduct)
    MyTextView_Roboto_Regular nameProduct;

    @BindView(R.id.priceProduct)
    MyTextView_Roboto_Regular priceProduct;

    @BindView(R.id.categoryPartner)
    MyTextView_Roboto_Regular categoryPartner;

    @BindView(R.id.descriptionProduct)
    MyTextView_Roboto_Regular descriptionProduct;

    @BindView(R.id.btnNumberProduct)
    ElegantNumberButton btnNumberProduct;

    @BindView(R.id.btnEditIngre)
    Button btnEditIngre;

    @BindView(R.id.btnAddIngredients)
    Button btnAddIngredients;

    @BindView(R.id.btnAddPedido)
    ExtendedFloatingActionButton btnAddPedido;

    private APIService apiService;
    private SharedPreferences sharedPref;
    private Context context;
    private DBHelper db;

    private String token = "";

    private String product_id = "";
    private String type_product = "";
    private Product product;

    private int numberProducts = 1;
    private String desciption = "";
    private String extra = "0";

    private Ingredient[] ingredientsResultModified;
    private Ingredient[] ingredientsResultAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        configToolbar();
        initVars();

    }

    public void configToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    private void initVars() {
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(
                "credentials", Context.MODE_PRIVATE);
        token = "Bearer " + sharedPref.getString(("token"), "noLogged");
        apiService = ApiUtils.getAPIService();
        db = new DBHelper(context);

        product_id = getIntent().getStringExtra("product_id");
        type_product = getIntent().getStringExtra("type_product");
        btnNumberProduct.setNumber("1");
        btnNumberProduct.setRange(1, 50);


    }

    @Override
    protected void onStart() {
        super.onStart();
        loadDataInView();
        events();

    }

    private void events() {


        btnNumberProduct.setOnClickListener((View.OnClickListener) v -> {
            String number = btnNumberProduct.getNumber();

            btnNumberProduct.setNumber(number);
        });

        btnNumberProduct.setOnValueChangeListener((view, oldValue, newValue) -> numberProducts = newValue);

        btnAddPedido.setOnClickListener(v -> {

            if (product != null) {


                showConfirmDialog();


            }

        });

        btnAddIngredients.setOnClickListener(v -> showDialogAddIngredients());

        btnEditIngre.setOnClickListener(v -> showDialogModifyIngredients());

    }

    private void loadDataInView() {

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("product_id", product_id)
                .addFormDataPart("type_product", type_product)
                .build();

        apiService.getProductClient("Accept", token, body).enqueue(new Callback<Product>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<Product> call, @NotNull Response<Product> response) {

                if (response.isSuccessful()) {

                    if (Objects.requireNonNull(response.body()).getStatus().equals("success")) {

                        product = response.body();


                        toolbar.setTitle(product.getName() + " - " + product.getPartner().getName());
                        nameProduct.setText(product.getName());
                        priceProduct.setText("Precio: " + product.getPrice() + " MXN");
                        categoryPartner.setText(product.getCategoria().getDescription() + " - " + product.getPartner().getName());

                        Glide.with(context).load(product.getImage()).into(imageProduct);
                        loadIngredientsInView(product.getDescription());


                    }


                }

            }

            @Override
            public void onFailure(@NotNull Call<Product> call, @NotNull Throwable t) {

            }
        });


    }

    private void loadIngredientsInView(String description) {
        int size = product.getIngredients().size();

        StringBuilder textComplete = new StringBuilder(description + "\n\nINGREDIENTES:\n\n");

        for (int i = 0; i < size; i++) {
            textComplete.append(product.getIngredients().get(i).getIngredient().getDescription()).append("\n");
        }

        descriptionProduct.setText(textComplete);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);


        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("product_id", product_id)
                .build();
        apiService.verifyFavoriteProducts("Accept", token, body).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {


                if (response.isSuccessful()) {

                    if (Objects.requireNonNull(response.body()).getStatus().equals("success")) {


                        if (response.body().getMessage().equals("existe")) {
                            menu.getItem(0).getIcon().setColorFilter(getResources().getColor(R.color.cal), PorterDuff.Mode.SRC_IN);
                        } else {
                            menu.getItem(0).getIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                        }


                    }

                }


            }

            @Override
            public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
            case R.id.action_like:

                actionLike(item);
                break;
            case R.id.action_share:
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    public void actionLike(MenuItem item) {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("product_id", product_id)
                .build();
        apiService.favoriteProduct("Aceept", token, body).enqueue(new Callback<ResponseDefault>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {

                if (response.isSuccessful()) {


                    if (Objects.requireNonNull(response.body()).getStatus().equals("success")) {

                        Toasty.success(context, response.body().getMessage(), Toasty.LENGTH_LONG).show();
                        if (response.body().getMessage().equals("Haz quitado el producto de tus favoritos")) {
                            item.getIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

                        } else {
                            item.getIcon().setColorFilter(getResources().getColor(R.color.cal), PorterDuff.Mode.SRC_IN);

                        }


                    } else {
                        Toasty.error(context, response.body().getMessage(), Toasty.LENGTH_LONG).show();
                    }


                }

            }

            @Override
            public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {
                Toasty.error(context, Objects.requireNonNull(t.getMessage()), Toasty.LENGTH_LONG).show();
            }
        });
    }


    private void showConfirmDialog() {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.confirm_product_dialog);

        Button cancelBtn = (Button) dialog.findViewById(R.id.btnCancel);
        Button addBtn = (Button) dialog.findViewById(R.id.btnAdd);

        setDataInDialog(dialog);

        cancelBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });


        addBtn.setOnClickListener(v -> {

            db.insertProduct(
                    product.getId(),
                    product.getName(),
                    product.getImage(),
                    product.getPrice(),
                    product.getId_partner(),
                    product.getPartner().getName(),
                    product.getType_product(),
                    desciption,
                    String.valueOf(numberProducts),
                    extra

            );

            dialog.dismiss();
            btnAddPedido.setVisibility(View.GONE);
            Toasty.success(context, "Haz agregado un producto a tu cesta").show();
        });

        dialog.show();

    }


    @SuppressLint("SetTextI18n")
    private void setDataInDialog(Dialog dialog) {

        TextView numProductsTextView = (TextView) dialog.findViewById(R.id.num);
        TextView detailDescriptionTextView = (TextView) dialog.findViewById(R.id.detail_description);
        numProductsTextView.setText("Número de productos: " + numberProducts );
        TextView priceDialg = (TextView) dialog.findViewById(R.id.price);

        String ingredientesInit = getIngredientsInit();
        String ingredientesExtra = getIngredientsExtra();
        desciption = ingredientesInit + ingredientesExtra;
        detailDescriptionTextView.setText(ingredientesInit + ingredientesExtra);

        float finalCost = (numberProducts * Float.parseFloat(product.getPrice())) + Float.parseFloat(extra);

        priceDialg.setText("Costo total: "+finalCost+ " MXN");

    }


    private String getIngredientsInit() {
        String ingred = "";

        if (ingredientsResultModified != null) {

            for (int i = 0; i < numberProducts; i++) {

                if (ingredientsResultModified[i] != null) {
                    ingred = ingred + "Ingredientes iniciales de la pieza: " + (i + 1) + "\n";
                    for (int j = 0; j < ingredientsResultModified[i].getIngredients().size(); j++) {


                        if (j < (ingredientsResultModified[i].getIngredients().size() - 1)) {
                            ingred = ingred + "" + ingredientsResultModified[i].getIngredients().get(j).getDescription() + ", ";
                        } else {
                            ingred = ingred + "" + ingredientsResultModified[i].getIngredients().get(j).getDescription() + "\n\n";
                        }


                    }
                }


            }
        } else {
            ingred = "Ingredientes base no modificados\n";
        }

        return ingred;
    }


    private String getIngredientsExtra() {
        String ingre = "";
        float extraAux = 0;

        if (ingredientsResultAdded != null) {

            for (int i = 0; i < numberProducts; i++) {



                if (ingredientsResultAdded[i] != null) {
                    ingre = ingre + "Ingredientes extra de la pieza " + (i + 1) + "\n";
                    for (int j = 0; j < ingredientsResultAdded[i].getIngredients().size(); j++) {

                        extraAux = extraAux + Float.parseFloat(ingredientsResultAdded[i].getIngredients().get(j).getPrice());
                        if (j < (ingredientsResultAdded[i].getIngredients().size() - 1)) {
                            ingre = ingre + "" + ingredientsResultAdded[i].getIngredients().get(j).getDescription() + " Extra, ";
                        } else {
                            ingre = ingre + "" + ingredientsResultAdded[i].getIngredients().get(j).getDescription() + " Extra\n\n";
                        }


                    }
                }


            }


        } else {
            extra = "0";
            ingre = ingre + "No hay ingredientes extra\n";
        }

        extra = String.valueOf(extraAux);
        return ingre;
    }




    public void showDialogAddIngredients() {
        @SuppressLint("CommitTransaction")
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialogAdd");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment newFragment = new AddIngredientsFragment().newInstance(numberProducts, token, product.getPartner().getId());
        newFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        newFragment.show(getSupportFragmentManager(), "dialogAdd");


    }

    public void showDialogModifyIngredients() {
        @SuppressLint("CommitTransaction")
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialogEdit");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment newFragment = new ModifyIngredientsFragment().newInstance(numberProducts, (Serializable) product.getIngredients());
        newFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        newFragment.show(getSupportFragmentManager(), "dialogEdit");


    }


    public void getDataFromModifyFragment(int size, Ingredient[] ingredient) {
        ingredientsResultModified = ingredient;
    }

    public void getDataFromAddFragment(int size, Ingredient[] ingredient) {
        ingredientsResultAdded = ingredient;
    }


}
