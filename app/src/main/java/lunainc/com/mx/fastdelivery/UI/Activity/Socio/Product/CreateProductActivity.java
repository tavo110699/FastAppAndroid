package lunainc.com.mx.fastdelivery.UI.Activity.Socio.Product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import lunainc.com.mx.fastdelivery.R;import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tiper.MaterialSpinner;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.Categoria;
import lunainc.com.mx.fastdelivery.Model.Ingredient;
import lunainc.com.mx.fastdelivery.Model.ResponseDefault;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProductActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.previewImage)
    ImageView previewImage;

    @BindView(R.id.btnActionImage)
    FloatingActionButton btnActionImage;

    @BindView(R.id.nameProduct)
    TextInputEditText nameProduct;

    @BindView(R.id.descriptionProduct)
    TextInputEditText descriptionProduct;

    @BindView(R.id.priceProduct)
    TextInputEditText priceProduct;

    @BindView(R.id.spinnerType)
    MaterialSpinner spinnerType;

    @BindView(R.id.spinnerIngredients)
    MultiSpinnerSearch spinnerIngredients;

    @BindView(R.id.actionBtn)
    ExtendedFloatingActionButton actionBtn;

    private StorageReference mImageStorage;
    private ProgressDialog mProgress;
    private static final int GALLERY_PICK = 1;


    private APIService apiService;
    private SharedPreferences sharedPref;
    private Context context;

    private String downloadULRImage = "";
    private String token = "";

    private List<KeyPairBoolData> spinnerItemsIngredients;
    private ArrayList<Ingredient> ingredients;

    private ArrayList<String> spinnerItemCategory;
    private ArrayList<Categoria> categorias;


    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        ButterKnife.bind(this);
        configToolbar();
        initVars();
        permisos(this);


    }

    public void configToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.add_product_title);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    private void initVars() {
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(
                "credentials", Context.MODE_PRIVATE);
        token = "Bearer " + sharedPref.getString(("token"), "noLogged");
        mImageStorage = FirebaseStorage.getInstance().getReference();

        apiService = ApiUtils.getAPIService();

        spinnerItemsIngredients = new ArrayList<>();
        spinnerItemCategory = new ArrayList<String>();
        ingredients = new ArrayList<Ingredient>();
        categorias = new ArrayList<Categoria>();
        populateSpinners();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerItemCategory);
        spinnerType.setAdapter(adapter);



        spinnerIngredients.setEmptyTitle("Aún no has selecccionado nada");
        spinnerIngredients.setSearchHint("Buscar ingrediente");

        spinnerIngredients.setItems(spinnerItemsIngredients,  -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

            }
        });


    }


    public void populateSpinners(){


        apiService.getIngredients("Acccept", token).enqueue(new Callback<Ingredient>() {
            @Override
            public void onResponse(@NotNull Call<Ingredient> call, @NotNull Response<Ingredient> response) {


                if (response.isSuccessful()) {
                    int tam = response.body().getIngredients().size();

                    if (tam > 0) {

                        for (int i = 0; i < tam; i++) {
                            Ingredient ingredient = (Ingredient) response.body().getIngredients().get(i);
                            ingredients.add(ingredient);
                            KeyPairBoolData h = new KeyPairBoolData();
                            h.setId(i);
                            h.setName(ingredient.getDescription());
                            h.setSelected(false);
                            spinnerItemsIngredients.add(h);

                        }



                    }


                }

            }

            @Override
            public void onFailure(@NotNull Call<Ingredient> call, @NotNull Throwable t) {
                Toasty.warning(context, "Ocurrio un error: " + t.getMessage(), Toast.LENGTH_SHORT, true).show();

            }
        });



        apiService.getCategorias("Accept", token).enqueue(new Callback<Categoria>() {
            @Override
            public void onResponse(@NotNull Call<Categoria> call, @NotNull Response<Categoria> response) {


                if (response.isSuccessful()) {

                    if (response.body().getStatus().equals("success")) {

                        int tam = response.body().getCategorias().size();

                        if (tam > 0) {
                            for (int i = 0; i < tam; i++) {
                                Categoria categoria = response.body().getCategorias().get(i);
                                spinnerItemCategory.add(categoria.getDescription());
                                categorias.add(categoria);
                            }
                        }

                    }


                }

            }

            @Override
            public void onFailure(@NotNull Call<Categoria> call, @NotNull Throwable t) {
                Toasty.warning(context, "Ocurrio un error: " + t.getMessage(), Toast.LENGTH_SHORT, true).show();

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        events();
    }

    private void events() {

        btnActionImage.setOnClickListener(view -> {
            Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(galleryIntent, "ELEGIR IMAGEN"), GALLERY_PICK);
        });


        actionBtn.setOnClickListener(view -> {

            String name = nameProduct.getText().toString();
            String description = descriptionProduct.getText().toString();
            String price = priceProduct.getText().toString();
            int sizeType = spinnerType.getSelection();
            int sizeIngredients = spinnerIngredients.getSelectedIds().size();


            if ( (name.trim().length() > 4 && description.trim().length() > 10
                    && price.trim().length() > 0) && (!downloadULRImage.isEmpty()) &&
                    (sizeType > -1)  && ( sizeIngredients > 0) ){

                String str = "";
                for (int i = 0; i < sizeIngredients; i++) {

                    if (i >= (sizeIngredients-1)){
                        str = str + ingredients.get(spinnerIngredients.getSelectedIds().get(i).intValue()).getId();

                    }else{
                        str = str + ingredients.get(spinnerIngredients.getSelectedIds().get(i).intValue()).getId()+",";
                    }

                }


                RequestBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("type_product", categorias.get( spinnerType.getSelection()).getId())
                        .addFormDataPart("name", name)
                        .addFormDataPart("description", description)
                        .addFormDataPart("price", price)
                        .addFormDataPart("image", downloadULRImage)
                        .addFormDataPart("ingredients", str)
                        .build();


                apiService.createProduct("Accept", token, body).enqueue(new Callback<ResponseDefault>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {

                        if (response.isSuccessful()){


                            if (response.body().getStatus().equals("success")){
                                clearInputs();
                                Toasty.success(context, response.body().getMessage(), Toast.LENGTH_SHORT, true).show();

                            }else{
                                Toasty.error(context, response.body().getMessage(), Toast.LENGTH_SHORT, true).show();

                            }

                        }




                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {
                        Toasty.warning(context, "Ocurrio un error: " + t.getMessage(), Toast.LENGTH_SHORT, true).show();

                    }
                });












            }else {
                Toasty.warning(context, "Todos los campos son necesarios", Toasty.LENGTH_LONG).show();
            }




        });


    }



    public void clearInputs(){
        nameProduct.setText(null);
        descriptionProduct.setText(null);
        priceProduct.setText(null);
        spinnerType.setSelected(false);
        spinnerIngredients.setSelected(false);

        previewImage.setImageDrawable(getDrawable(R.drawable.ic_foto));
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

    public void gotToBack() {
        new Constants().goTo(this, ProductsPartnerActivity.class);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .start(CreateProductActivity.this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mProgress = new ProgressDialog(CreateProductActivity.this);
                mProgress.setTitle("Cargando imagen");
                mProgress.setMessage("Espere un momento por favor");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();


                Uri resultUri = result.getUri();

                File thumb_filePath = new File(resultUri.getPath());

                //comprimir imagen de perfil
                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(75)
                        .compressToBitmap(thumb_filePath);

                previewImage.setImageBitmap(thumb_bitmap);


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                int leftLimit = 97; // letter 'a'
                int rightLimit = 122; // letter 'z'
                int targetStringLength = 10;
                Random random = new Random();
                StringBuilder buffer = new StringBuilder(targetStringLength);
                for (int i = 0; i < targetStringLength; i++) {
                    int randomLimitedInt = leftLimit + (int)
                            (random.nextFloat() * (rightLimit - leftLimit + 1));
                    buffer.append((char) randomLimitedInt);
                }
                String randomStringBuilder = buffer.toString();


                final StorageReference filepath = mImageStorage.child("products").child(randomStringBuilder + ".jpg");


                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                        if (task.isSuccessful()) {


                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadULRImage = uri.toString();
                                    mProgress.dismiss();
                                }
                            });


                        } else {
                            mProgress.dismiss();
                            Toasty.warning(CreateProductActivity.this, "Opss ocurrio un error :(", Toasty.LENGTH_LONG).show();
                        }


                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }


        }


    }


    public void permisos(Activity activity) {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }


            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }


        }


    }


}
